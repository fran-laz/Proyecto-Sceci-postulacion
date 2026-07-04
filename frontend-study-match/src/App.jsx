import { Routes, Route, Link } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';
import Registro from './pages/Registro';

function App() {
  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };
  return (
    <div>
      <h1 style={{ textAlign: 'center', marginTop: '10px' }}>Study Match</h1>
      
      <nav style={{ marginBottom: '20px', padding: '10px', background: '#ddd', display: 'flex', justifyContent: 'center', gap: '15px' }}>
        <Link to="/">Inicio</Link>
        <Link to="/login">Login</Link>
        <Link to="/registro">Registro</Link>
        <Link to="/dashboard">Dashboard</Link>
        {localStorage.getItem('token') && (
          <button onClick={handleLogout} style={{ cursor: 'pointer', background: 'transparent', border: 'none', color: 'blue', textDecoration: 'underline' }}>
            Cerrar Sesión
          </button>
        )}
      </nav>

      <Routes>
          <Route path="/" element={<p style={{ textAlign: 'center' }}>Estás en la página de inicio.</p>} />
          <Route path="/login" element={<Login />} />
          <Route path="/registro" element={<Registro />} /> 
  
          <Route 
              path="/dashboard" 
              element={
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              } 
            />
          </Routes>
    </div>
  );
}

export default App;