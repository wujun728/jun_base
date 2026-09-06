import axios from 'axios';

interface SysFile {
    dirName: string;
    sysTypeName: string;
    typeName: string;
    total: string;
    free: string;
    used: string;
    usage: number;
}

interface Sys {
    computerName: string;
    computerIp: string;
    userDir: string;
    osName: string;
    osArch: string;
}

interface Jvm {
    total: number;
    max: number;
    free: number;
    version: string;
    home: string;
    name: string;
    used: number;
    startTime: string;
    usage: number;
    runTime: string;
    inputArgs: string;
}

interface Mem {
    total: number;
    used: number;
    free: number;
    usage: number;
}

interface Cpu {
    cpuNum: number;
    total: number;
    sys: number;
    used: number;
    wait: number;
    free: number;
}

export interface ServerRecord {
    cpu: Cpu;
    mem: Mem;
    jvm: Jvm;
    sys: Sys;
    sysFiles: SysFile[];
}

// 获取服务信息
export function getServer() {
    return axios({
        url: '/monitor/server',
        method: 'get'
    })
}
