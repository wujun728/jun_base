import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'

export default defineConfig(() => {
    return {
        base: './',
        build: {
            minify: true,
            outDir: 'dist',
        },
        server: {
            port: '3002'
        },
        plugins: [
            uni()
        ],
        exclude:[
            /\/README\.md$/,
        ]
    }
})