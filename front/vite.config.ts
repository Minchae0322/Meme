import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },

  css: {
    // Element Plus의 CSS 파일을 직접 가져오도록 지정
    preprocessorOptions: {
      scss: {
        additionalData: `@import "./node_modules/element-plus/packages/theme-chalk/src/index.scss";`
      }
    }
  }

})


