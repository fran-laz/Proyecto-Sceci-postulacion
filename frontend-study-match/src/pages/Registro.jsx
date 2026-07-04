import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

export default function Registro() {
  const [nombres, setNombres] = useState('');
  const [apellidos, setApellidos] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [carrera, setCarrera] = useState(''); 
  
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const datosRegistro = {
      email: email,
      password: password,
      nombres: nombres,
      apellidos: apellidos,
      carrera: carrera
    };

    try {
      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(datosRegistro),
      });

      if (response.ok) {
        alert('¡Registro exitoso! Ya puedes iniciar sesión.');
        navigate('/login'); 
      } else {
        const errorMsg = await response.text();
        setError(`Error: ${errorMsg || 'No se pudo crear la cuenta'}`);
      }
    } catch (err) {
      setError('Error de conexión. ¿Está encendido el servidor Spring Boot?');
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={{ marginBottom: '20px', textAlign: 'center' }}>Crear Cuenta</h2>
        
        {error && <p style={styles.error}>{error}</p>}

        <form onSubmit={handleSubmit} style={styles.form}>
          
          <div style={styles.row}>
            <div style={{ ...styles.inputGroup, flex: 1, marginRight: '10px' }}>
              <label>Nombres:</label>
              <input type="text" required value={nombres} onChange={(e) => setNombres(e.target.value)} style={styles.input} />
            </div>
            <div style={{ ...styles.inputGroup, flex: 1 }}>
              <label>Apellidos:</label>
              <input type="text" required value={apellidos} onChange={(e) => setApellidos(e.target.value)} style={styles.input} />
            </div>
          </div>

          <div style={styles.inputGroup}>
            <label>Carrera:</label>
            <select required value={carrera} onChange={(e) => setCarrera(e.target.value)} style={styles.input}>
              <option value="">-- Selecciona tu carrera --</option>
              <option value="1">Ingeniería Informática</option>
              <option value="2">Ingeniería de Sistemas</option>
            </select>
          </div>

          <div style={styles.inputGroup}>
            <label>Correo:</label>
            <input type="email" required value={email} onChange={(e) => setEmail(e.target.value)} style={styles.input} />
          </div>

          <div style={styles.inputGroup}>
            <label>Contraseña:</label>
            <input type="password" required value={password} onChange={(e) => setPassword(e.target.value)} style={styles.input} />
          </div>

          <button type="submit" style={styles.button}>Registrarme</button>
        </form>
        
        <p style={{ marginTop: '20px', textAlign: 'center', fontSize: '14px' }}>
          ¿Ya tienes cuenta? <Link to="/login" style={{ color: '#0056b3', textDecoration: 'none', fontWeight: 'bold' }}>Inicia sesión</Link>
        </p>
      </div>
    </div>
  );
}

const styles = {
  container: { display: 'flex', justifyContent: 'center', marginTop: '40px', paddingBottom: '30px' },
  card: { background: 'white', padding: '30px', borderRadius: '8px', boxShadow: '0 4px 12px rgba(0,0,0,0.1)', width: '420px' },
  form: { display: 'flex', flexDirection: 'column' },
  row: { display: 'flex', justifyContent: 'space-between' },
  inputGroup: { marginBottom: '15px', display: 'flex', flexDirection: 'column' },
  input: { padding: '10px', marginTop: '5px', borderRadius: '4px', border: '1px solid #ccc', fontSize: '15px' },
  button: { padding: '12px', background: '#28a745', color: 'white', border: 'none', borderRadius: '4px', fontSize: '16px', cursor: 'pointer', fontWeight: 'bold', marginTop: '10px' },
  error: { color: '#721c24', fontSize: '14px', marginBottom: '15px', textAlign: 'center', background: '#f8d7da', padding: '10px', borderRadius: '4px', border: '1px solid #f5c6cb' }
};