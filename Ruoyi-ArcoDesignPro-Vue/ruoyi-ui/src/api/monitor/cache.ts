import axios from 'axios';

interface Info {
    aof_rewrite_scheduled: string;
    number_of_cached_scripts: string;
    mem_clients_slaves: string;
    keyspace_misses: string;
    mem_allocator: string;
    multiplexing_api: string;
    aof_last_write_status: string;
    used_memory_peak_perc: string;
    run_id: string;
    uptime_in_seconds: string;
    maxmemory: string;
    active_defrag_hits: string;
    atomicvar_api: string;
    total_system_memory_human: string;
    second_repl_offset: string;
    arch_bits: string;
    master_repl_offset: string;
    instantaneous_output_kbps: string;
    total_commands_processed: string;
    repl_backlog_size: string;
    sync_full: string;
    sync_partial_err: string;
    aof_last_cow_size: string;
    configured_hz: string;
    used_memory_startup: string;
    mem_clients_normal: string;
    used_cpu_user: string;
    aof_enabled: string;
    redis_mode: string;
    rdb_changes_since_last_save: string;
    used_memory_lua: string;
    redis_build_id: string;
    connected_slaves: string;
    expired_keys: string;
    used_memory: string;
    active_defrag_misses: string;
    active_defrag_key_misses: string;
    used_memory_rss: string;
    rdb_last_bgsave_status: string;
    process_id: string;
    rejected_connections: string;
    master_replid: string;
    used_memory_rss_human: string;
    used_memory_scripts: string;
    rdb_bgsave_in_progress: string;
    rdb_last_cow_size: string;
    cluster_enabled: string;
    tcp_port: string;
    os: string;
    mem_replication_backlog: string;
    aof_current_rewrite_time_sec: string;
    allocator_rss_ratio: string;
    used_memory_overhead: string;
    repl_backlog_histlen: string;
    slave_expires_tracked_keys: string;
    used_cpu_sys: string;
    role: string;
    used_memory_dataset_perc: string;
    expired_stale_perc: string;
    allocator_frag_ratio: string;
    client_recent_max_input_buffer: string;
    aof_last_bgrewrite_status: string;
    used_memory_peak_human: string;
    hz: string;
    used_memory_human: string;
    maxmemory_policy: string;
    rss_overhead_ratio: string;
    mem_fragmentation_ratio: string;
    rdb_last_bgsave_time_sec: string;
    allocator_allocated: string;
    used_memory_dataset: string;
    blocked_clients: string;
    used_cpu_sys_children: string;
    repl_backlog_active: string;
    used_cpu_user_children: string;
    sync_partial_ok: string;
    aof_rewrite_in_progress: string;
    redis_git_sha1: string;
    rdb_current_bgsave_time_sec: string;
    active_defrag_running: string;
    aof_last_rewrite_time_sec: string;
    master_replid2: string;
    config_file: string;
    lazyfree_pending_objects: string;
    used_memory_scripts_human: string;
    loading: string;
    db9: string;
    db5: string;
    db4: string;
    allocator_rss_bytes: string;
    db3: string;
    pubsub_channels: string;
    db1: string;
    used_memory_lua_human: string;
    db0: string;
    active_defrag_key_hits: string;
    allocator_frag_bytes: string;
    migrate_cached_sockets: string;
    redis_git_dirty: string;
    total_system_memory: string;
    mem_aof_buffer: string;
    rss_overhead_bytes: string;
    client_recent_max_output_buffer: string;
    connected_clients: string;
    mem_fragmentation_bytes: string;
    lru_clock: string;
    pubsub_patterns: string;
    expired_time_cap_reached_count: string;
    repl_backlog_first_byte_offset: string;
    total_net_output_bytes: string;
    executable: string;
    evicted_keys: string;
    total_connections_received: string;
    allocator_active: string;
    mem_not_counted_for_evict: string;
    redis_version: string;
    total_net_input_bytes: string;
    latest_fork_usec: string;
    maxmemory_human: string;
    used_memory_peak: string;
    allocator_resident: string;
    keyspace_hits: string;
    uptime_in_days: string;
    instantaneous_input_kbps: string;
    instantaneous_ops_per_sec: string;
    rdb_last_save_time: string;
}

interface CommandStat {
    name: string;
    value: string;
}

export interface CacheData {
    commandStats: CommandStat[];
    info: Info;
    dbSize: number;
}

export interface CacheRecord {
    cacheName: string;
    cacheKey: string;
    cacheValue: string;
    remark: string;
}

// 查询缓存详细
export function getCache() {
    return axios({
        url: '/monitor/cache',
        method: 'get'
    })
}

// 查询缓存名称列表
export function listCacheName() {
    return axios({
        url: '/monitor/cache/getNames',
        method: 'get'
    })
}

// 查询缓存键名列表
export function listCacheKey(cacheName: string) {
    return axios({
        url: `/monitor/cache/getKeys/${cacheName}`,
        method: 'get'
    })
}

// 查询缓存内容
export function getCacheValue(cacheName: string, cacheKey: string) {
    return axios({
        url: `/monitor/cache/getValue/${cacheName}/${cacheKey}`,
        method: 'get'
    })
}

// 清理指定名称缓存
export function clearCacheName(cacheName: string) {
    return axios({
        url: `/monitor/cache/clearCacheName/${cacheName}`,
        method: 'delete'
    })
}

// 清理指定键名缓存
export function clearCacheKey(cacheKey: string) {
    return axios({
        url: `/monitor/cache/clearCacheKey/${cacheKey}`,
        method: 'delete'
    })
}

// 清理全部缓存
export function clearCacheAll() {
    return axios({
        url: '/monitor/cache/clearCacheAll',
        method: 'delete'
    })
}
