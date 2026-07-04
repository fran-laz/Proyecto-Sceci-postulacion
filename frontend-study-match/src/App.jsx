import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';
import Registro from './pages/Registro';
import Inicio from './pages/Inicio';

function App() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };
  return (
    <div>
      <h1 style={{ textAlign: 'center', marginTop: '10px' }}>Study Match</h1>
      
      <nav style={{ marginBottom: '20px', padding: '10px', background: '#ddd', display: 'flex', justifyContent: 'center', gap: '15px' }}>
        {token ? (
          <>
            <Link to="/">Inicio</Link>
            <Link to="/dashboard">Dashboard</Link>
            <button onClick={handleLogout} style={{ cursor: 'pointer', background: 'transparent', border: 'none', color: 'red', textDecoration: 'underline', fontWeight: 'bold' }}>
              Cerrar Sesión
            </button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/registro">Registro</Link>
          </>
        )}
      </nav>

      <Routes>
          <Route path="/" element={
            <ProtectedRoute>
              <Inicio/>
            </ProtectedRoute>
          } />
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