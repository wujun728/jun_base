import { defineConfig, loadEnv, UserConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path, { resolve } from 'path';

function pathResolve(dir) {
  // console.log(process.cwd());
  return resolve(process.cwd(), '.', dir);
}

export default ({ mode }) => {
  return defineConfig({
    plugins: [vue()],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },
    base: loadEnv(mode, process.cwd()).VITE_BASE_URL,
    // root: '',
    server: {
      host: '0.0.0.0',
      port: parseInt(loadEnv(mode, process.cwd()).VITE_PORT),
      open: true,
      proxy: {
        '/dev_api': {
          // target: 'http://localhost:8901',
          target: 'http://localhost:8080',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/dev_api/, ''),
        },
      },
    },
    // resolve: {
    //   alias: {
    //     "@": path.resolve(__dirname, "src"),
    //     components: path.resolve(__dirname, "src/components"),
    //     styles: path.resolve(__dirname, "src/styles"),
    //     plugins: path.resolve(__dirname, "src/plugins"),
    //     views: path.resolve(__dirname, "src/views"),
    //     layouts: path.resolve(__dirname, "src/layouts"),
    //     utils: path.resolve(__dirname, "src/utils"),
    //     apis: path.resolve(__dirname, "src/apis"),
    //     dirs: path.resolve(__dirname, "src/directives"),
    //   },
    // },

    build: {
      target: 'es2015',
      // outDir: "../docs",
      // emptyOutDir: true,
    },
  });
};
