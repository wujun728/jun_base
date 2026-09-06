import request from '@/utils/http'
import { add, get, list, remove, update } from './base'
import type { Entity, Query } from './types'

const modelRoot = '/system/artbot/model'

export interface ArtBotModel extends Entity {
  modelId: number
  modelName: string
  modelCode: string
  status: string
  isDefault: string
}

export interface ArtBotConversation extends Entity {
  conversationId: number
  modelId: number
  title: string
  modelName: string
  modelCode: string
}

export interface ArtBotMessage {
  messageId: number | string
  conversationId: number
  role: 'user' | 'assistant'
  content: string
  createTime?: string
}

export const fetchArtBotModelList = (params: Query) =>
  list<ArtBotModel>(`${modelRoot}/list`, params)
export const fetchArtBotModel = (id: number) => get<ArtBotModel>(`${modelRoot}/${id}`)
export const addArtBotModel = (data: Entity) => add(modelRoot, data)
export const updateArtBotModel = (data: Entity) => update(modelRoot, data)
export const removeArtBotModel = (id: number | string) => remove(`${modelRoot}/${id}`)
export const setDefaultArtBotModel = (id: number) =>
  request.put<void>({ url: `${modelRoot}/${id}/default`, showSuccessMessage: true })
export const testArtBotModel = (id: number) =>
  request.post<void>({ url: `${modelRoot}/${id}/test`, showSuccessMessage: true })

export const fetchAvailableArtBotModels = () =>
  request.get<ArtBotModel[]>({ url: '/artbot/models' })
export const fetchArtBotConversations = () =>
  request.get<ArtBotConversation[]>({ url: '/artbot/conversations' })
export const createArtBotConversation = (modelId: number) =>
  request.post<ArtBotConversation>({
    url: '/artbot/conversations',
    data: { modelId }
  })
export const removeArtBotConversation = (id: number) =>
  request.del<void>({ url: `/artbot/conversations/${id}` })
export const fetchArtBotMessages = (id: number) =>
  request.get<ArtBotMessage[]>({
    url: `/artbot/conversations/${id}/messages`
  })

export async function streamArtBotMessage(
  conversationId: number,
  content: string,
  token: string,
  onChunk: (chunk: string) => void,
  signal: AbortSignal
) {
  const response = await fetch(`${import.meta.env.VITE_API_URL}/artbot/chat`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ conversationId, content }),
    signal
  })
  if (!response.ok || !response.body) {
    throw new Error((await response.text()) || `请求失败：${response.status}`)
  }
  const reader = response.body.getReader()
  const decoder = new TextDecoder()
  let buffer = ''
  let receivedChunk = false
  try {
    while (true) {
      const { value, done } = await reader.read()
      if (value) buffer += decoder.decode(value, { stream: !done })
      const events = buffer.replaceAll('\r\n', '\n').split('\n\n')
      buffer = events.pop() || ''
      for (const block of events) {
        let event = 'message'
        const data: string[] = []
        block.split('\n').forEach((line) => {
          if (line.startsWith('event:')) event = line.slice(6).trim()
          if (line.startsWith('data:')) data.push(line.slice(5).trimStart())
        })
        const payload = data.join('\n')
        if (event === 'chunk') {
          receivedChunk = true
          onChunk(payload)
        }
        if (event === 'error' && !receivedChunk) throw new Error(payload || '生成失败')
      }
      if (done) break
    }
  } catch (error) {
    if (receivedChunk) return
    throw error
  }
  if (!receivedChunk) throw new Error('模型未返回有效内容')
}
