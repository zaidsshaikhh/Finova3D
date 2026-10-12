import React, { useEffect, useRef } from 'react'

const AnimatedBackground = () => {
  const canvasRef = useRef(null)

  useEffect(() => {
    const canvas = canvasRef.current
    if (!canvas) return

    const ctx = canvas.getContext('2d')
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight

    let animationId
    let time = 0

    const particles = []
    const particleCount = 50

    class Particle {
      constructor() {
        this.x = Math.random() * canvas.width
        this.y = Math.random() * canvas.height
        this.z = Math.random() * 1000
        this.vx = (Math.random() - 0.5) * 2
        this.vy = (Math.random() - 0.5) * 2
        this.radius = Math.random() * 2 + 1
        this.color = ['#c77dff', '#ff006e', '#00f5ff', '#00ff88'][Math.floor(Math.random() * 4)]
      }

      update() {
        this.x += this.vx
        this.y += this.vy
        this.z -= 2

        if (this.x < 0) this.x = canvas.width
        if (this.x > canvas.width) this.x = 0
        if (this.y < 0) this.y = canvas.height
        if (this.y > canvas.height) this.y = 0
        if (this.z <= 0) this.z = 1000
      }

      draw(ctx) {
        const scale = 1 - this.z / 1000
        const radius = this.radius * scale
        const opacity = scale * 0.6

        ctx.fillStyle = this.color.replace(')', `, ${opacity})`).replace('rgb', 'rgba')
        ctx.beginPath()
        ctx.arc(this.x, this.y, Math.max(radius, 0.5), 0, Math.PI * 2)
        ctx.fill()
      }
    }

    for (let i = 0; i < particleCount; i++) {
      particles.push(new Particle())
    }

    const animate = () => {
      time += 0.005

      // Clear background with gradient
      const gradient = ctx.createLinearGradient(0, 0, canvas.width, canvas.height)
      gradient.addColorStop(0, '#0a0e27')
      gradient.addColorStop(0.5, '#1a1530')
      gradient.addColorStop(1, '#2d0a4a')
      ctx.fillStyle = gradient
      ctx.fillRect(0, 0, canvas.width, canvas.height)

      // Draw grid
      ctx.strokeStyle = 'rgba(199, 125, 255, 0.1)'
      ctx.lineWidth = 1
      const gridSize = 100
      for (let i = 0; i < canvas.width; i += gridSize) {
        ctx.beginPath()
        ctx.moveTo(i, 0)
        ctx.lineTo(i, canvas.height)
        ctx.stroke()
      }
      for (let i = 0; i < canvas.height; i += gridSize) {
        ctx.beginPath()
        ctx.moveTo(0, i)
        ctx.lineTo(canvas.width, i)
        ctx.stroke()
      }

      // Draw animated waves
      ctx.strokeStyle = 'rgba(199, 125, 255, 0.3)'
      ctx.lineWidth = 2
      for (let x = 0; x < canvas.width; x += 10) {
        const y = canvas.height / 2 + Math.sin(x * 0.01 + time) * 50 + Math.cos(time) * 30
        if (x === 0) {
          ctx.beginPath()
          ctx.moveTo(x, y)
        } else {
          ctx.lineTo(x, y)
        }
      }
      ctx.stroke()

      // Update and draw particles
      particles.forEach((particle) => {
        particle.update()
        particle.draw(ctx)
      })

      // Draw floating orbs
      const orbSize = 100
      for (let i = 0; i < 3; i++) {
        const orbX = canvas.width / 4 + (i * canvas.width / 3) + Math.sin(time + i) * 50
        const orbY = canvas.height / 3 + Math.cos(time * 0.7 + i) * 50
        const color = ['#c77dff', '#ff006e', '#00f5ff'][i]

        const orbGradient = ctx.createRadialGradient(orbX, orbY, 0, orbX, orbY, orbSize)
        orbGradient.addColorStop(0, color.replace('rgb', 'rgba').replace(')', ', 0.4)'))
        orbGradient.addColorStop(1, color.replace('rgb', 'rgba').replace(')', ', 0)'))
        ctx.fillStyle = orbGradient
        ctx.beginPath()
        ctx.arc(orbX, orbY, orbSize, 0, Math.PI * 2)
        ctx.fill()
      }

      animationId = requestAnimationFrame(animate)
    }

    animate()

    const handleResize = () => {
      canvas.width = window.innerWidth
      canvas.height = window.innerHeight
    }

    window.addEventListener('resize', handleResize)

    return () => {
      cancelAnimationFrame(animationId)
      window.removeEventListener('resize', handleResize)
    }
  }, [])

  return (
    <canvas
      ref={canvasRef}
      className="fixed top-0 left-0 w-full h-full -z-10"
    />
  )
}

export default AnimatedBackground
