import UnoCSS from 'unocss/vite';
import { loadEnv, type UserConfig } from 'vite';

const commonConfig: (mode: string) => UserConfig = (mode) => {
  const { VITE_DROP_CONSOLE } = loadEnv(mode, process.cwd());
  const dropConsole = VITE_DROP_CONSOLE === 'true';
  return {
    server: {
      host: true,
    },
    esbuild: {
      drop: mode === 'production' ? (dropConsole ? ['console', 'debugger'] : []) : [],
    },
    build: {
      reportCompressedSize: false,
      chunkSizeWarningLimit: 1500,
      rollupOptions: {
        // TODO: Prevent memory overflow
        maxParallelFileOps: 3,
      },
    },
    plugins: [UnoCSS()],
  };
};

export { commonConfig };
