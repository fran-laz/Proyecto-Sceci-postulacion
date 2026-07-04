import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault(); 
    setError(''); 

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('token', data.token);
        navigate('/dashboard');
      } else {
        setError('Credenciales incorrectas Verifica tu correo y contraseña.');
      }
    } catch (err) {
      setError('Error al conectar con el servidor');
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={{ marginBottom: '20px', textAlign: 'center' }}>Iniciar Sesión</h2>

        {error && <p style={styles.error}>{error}</p>}

        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.inputGroup}>
            <label>Correo Electrónico:</label>
            <input 
              type="email" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required 
              style={styles.input}
            />
          </div>

          <div style={styles.inputGroup}>
            <label>Contraseña:</label>
            <input 
              type="password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required 
              style={styles.input}
            />
          </div>

          <button type="submit" style={styles.button}>Entrar</button>
        </form>
      </div>
    </div>
  );
}
const styles = {
  container: { display: 'flex', justifyContent: 'center', marginTop: '50px' },
  card: { background: 'white', padding: '30px', borderRadius: '8px', boxShadow: '0 4px 8px rgba(0,0,0,0.1)', width: '350px' },
  form: { display: 'flex', flexDirection: 'column' },
  inputGroup: { marginBottom: '15px', display: 'flex', flexDirection: 'column' },
  input: { padding: '10px', marginTop: '5px', borderRadius: '4px', border: '1px solid #ccc', fontSize: '16px' },
  button: { padding: '10px', background: '#0056b3', color: 'white', border: 'none', borderRadius: '4px', fontSize: '16px', cursor: 'pointer', marginTop: '10px' },
  error: { color: 'red', fontSize: '14px', marginBottom: '15px', textAlign: 'center' }
};