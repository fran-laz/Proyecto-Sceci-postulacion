import { Routes, Route, Link } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';

function App() {
  return (
    <div>
      <h1>Bienvenido a Study Match</h1>
      <nav style={{ marginBottom: '20px', padding: '10px', background: '#ffffff' }}>
        <Link to="/" style={{ marginRight: '10px' }}>Inicio</Link>
        <Link to="/login" style={{ marginRight: '10px' }}>Login</Link>
        <Link to="/dashboard">Dashboard</Link>
      </nav>

      <Routes>
        <Route path="/" element={<p>Pagina de inicio.</p>} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </div>
  );
}

export default App;