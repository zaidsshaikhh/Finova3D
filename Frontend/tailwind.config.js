/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx}",
  ],
  theme: {
    extend: {
      colors: {
        dark: {
          900: '#0a0e27',
          800: '#1a1f3a',
          700: '#2a2f4a',
          600: '#3a3f5a',
        },
        neon: {
          purple: '#c77dff',
          pink: '#ff006e',
          cyan: '#00f5ff',
          green: '#00ff88',
        },
      },
      animation: {
        float: 'float 6s ease-in-out infinite',
        glow: 'glow 2s ease-in-out infinite',
        pulse: 'pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite',
      },
      keyframes: {
        float: {
          '0%, 100%': { transform: 'translateY(0px)' },
          '50%': { transform: 'translateY(20px)' },
        },
        glow: {
          '0%, 100%': { boxShadow: '0 0 5px rgba(199, 125, 255, 0.5)' },
          '50%': { boxShadow: '0 0 20px rgba(199, 125, 255, 1)' },
        },
      },
    },
  },
  plugins: [],
}
