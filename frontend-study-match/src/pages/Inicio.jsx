import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

export default function Inicio() {
  const [proyectos, setProyectos] = useState([]);
  const [grupos, setGrupos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  const token = localStorage.getItem('token');

  useEffect(() => {
    const cargarMisActividades = async () => {
      try {
        const resProyectos = await fetch('http://localhost:8080/api/proyectos/mis-proyectos', {
          headers: { 'Authorization': `Bearer ${token}` }
        });
        
        const resGrupos = await fetch('http://localhost:8080/api/grupos-estudio/mis-grupos', {
          headers: { 'Authorization': `Bearer ${token}` }
        });

        if (resProyectos.ok && resGrupos.ok) {
          const dataProyectos = await resProyectos.json();
          const dataGrupos = await resGrupos.json();
          setProyectos(dataProyectos);
          setGrupos(dataGrupos);
        } else {
          setError('No se pudieron cargar tus actividades. Verifica tu conexión.');
        }
      } catch (err) {
        setError('Error al conectar con el servidor de Spring Boot.');
      } finally {
        setCargando(false);
      }
    };

    cargarMisActividades();
  }, [token]);

  if (cargando) return <div style={{ textAlign: 'center', marginTop: '50px' }}>Cargando tus actividades...</div>;

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>Mi Centro de Estudio</h2>
      
      {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}

      <div style={styles.grid}>
        <div style={styles.column}>
          <h3 style={styles.columnTitle}>Mis Proyectos</h3>
          {proyectos.length === 0 ? (
            <p style={styles.emptyText}>No estás en ningún proyecto aún. <Link to="/dashboard">Busca uno aquí.</Link></p>
          ) : (
            proyectos.map(proyecto => (
              <div key={proyecto.id} style={styles.card}>
                <h4>{proyecto.titulo}</h4>
                <p style={styles.materiaText}>Materia: {proyecto.nombreMateria}</p>
                <p style={styles.descText}>{proyecto.descripcion}</p>
                <p style={styles.dateText}><strong>Límite:</strong> {proyecto.fechaLimite}</p>
              </div>
            ))
          )}
        </div>
        <div style={styles.column}>
          <h3 style={styles.columnTitle}>Mis Grupos de Estudio</h3>
          {grupos.length === 0 ? (
            <p style={styles.emptyText}>No estás en ningún grupo de estudio. <Link to="/dashboard">Únete a uno.</Link></p>
          ) : (
            grupos.map(grupo => (
              <div key={grupo.id} style={styles.card}>
                <h4>{grupo.titulo}</h4>
                <p style={styles.materiaText}>Materia: {grupo.nombreMateria}</p>
                <p style={styles.descText}>{grupo.descripcion}</p>
                <p style={styles.dateText}><strong>Horario:</strong> {grupo.horarioHabitual}</p>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: { maxWidth: '900px', margin: '0 auto', padding: '20px' },
  title: { textAlign: 'center', marginBottom: '30px', color: '#333' },
  grid: { display: 'flex', gap: '20px', justifyContent: 'space-between' },
  column: { flex: 1, background: '#f8f9fa', padding: '20px', borderRadius: '8px', border: '1px solid #dee2e6' },
  columnTitle: { marginBottom: '15px', borderBottom: '2px solid #ccc', paddingBottom: '5px' },
  card: { background: 'white', padding: '15px', borderRadius: '6px', marginBottom: '15px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)', border: '1px solid #e9ecef' },
  materiaText: { fontSize: '13px', color: '#0056b3', fontWeight: 'bold', margin: '5px 0' },
  descText: { fontSize: '14px', color: '#555', marginBottom: '10px' },
  dateText: { fontSize: '13px', color: '#28a745' },
  emptyText: { fontSize: '14px', color: '#666', fontStyle: 'italic' }
};