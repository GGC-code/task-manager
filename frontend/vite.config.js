import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Tutte le richieste che iniziano con /api vengono inoltrate al back-end.
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})