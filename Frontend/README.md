# Finova 3D Frontend

Modern React-based frontend for Finova 3D expense management system with animated 3D backgrounds.

## Features

- **Animated 3D Backgrounds**: Custom canvas-based particle effects and wave animations
- **Dashboard**: Real-time financial overview with charts
- **Expense Management**: Track and categorize expenses
- **Income Tracking**: Monitor income sources
- **Budget Planning**: Set and track budgets
- **Financial Goals**: Create and monitor savings goals
- **Analytics**: Visual insights with charts
- **Dark Theme**: Modern dark UI with neon accents
- **Responsive Design**: Works on all screen sizes

## Setup

### Prerequisites
- Node.js 16+
- npm or yarn

### Installation

```bash
cd Frontend
npm install
```

### Development

```bash
npm run dev
```

Open http://localhost:3000 in your browser.

### Build

```bash
npm run build
```

## Environment Variables

Create `.env.local` in the `Frontend` directory:

```
VITE_API_URL=http://localhost:8080
```

## Project Structure

```
Frontend/
├── src/
│   ├── components/
│   │   ├── AnimatedBackground.jsx
│   │   ├── Sidebar.jsx
│   │   └── StatCard.jsx
│   ├── context/
│   │   └── AuthContext.jsx
│   ├── pages/
│   │   ├── Login.jsx
│   │   ├── Register.jsx
│   │   ├── Dashboard.jsx
│   │   ├── Expenses.jsx
│   │   ├── Income.jsx
│   │   ├── Budgets.jsx
│   │   ├── Goals.jsx
│   │   └── Analytics.jsx
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── index.html
├── package.json
├── tailwind.config.js
├── postcss.config.js
└── vite.config.js
```

## Technologies Used

- **React 18**: UI framework
- **Vite**: Build tool
- **Tailwind CSS**: Styling
- **Framer Motion**: Animations
- **Axios**: HTTP client
- **Chart.js**: Data visualization
- **React Router**: Navigation
- **Lucide Icons**: Icons

## Features Implemented

✅ Animated 3D particle background
✅ Modern glass-morphism UI
✅ JWT authentication
✅ Expense CRUD operations
✅ Income tracking
✅ Dashboard with charts
✅ Responsive design
✅ Dark theme with neon accents
✅ Sidebar navigation
✅ Real-time data integration

## TODO

- [ ] Complete budget management UI
- [ ] Complete goals management UI
- [ ] Complete analytics dashboard
- [ ] Receipt upload UI
- [ ] Recurring transactions UI
- [ ] Spending insights visualization
- [ ] Export to CSV
- [ ] Mobile-optimized views
