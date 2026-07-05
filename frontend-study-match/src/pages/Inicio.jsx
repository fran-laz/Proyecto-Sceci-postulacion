import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

export default function Inicio() {
  const [proyectos, setProyectos] = useState([]);
  const [grupos, setGrupos] = useState([]);
  const [solicitudes, setSolicitudes] = useState([]);
  
  const [integrantesVisibles, setIntegrantesVisibles] = useState(null); 
  const [listaIntegrantes, setListaIntegrantes] = useState([]);

  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  const [miEmail, setMiEmail] = useState('');

  const token = localStorage.getItem('token');

  useEffect(() => {
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        setMiEmail(payload.sub); 
      } catch (e) { console.error("Error leyendo token"); }
    }

    const cargarDatos = async () => {
      try {
        const headers = { 'Authorization': `Bearer ${token}` };
        
        const resProyectos = await fetch('http://localhost:8080/api/proyectos/mis-proyectos', { headers });
        const resGrupos = await fetch('http://localhost:8080/api/grupos-estudio/mis-grupos', { headers });
        const resSolicitudes = await fetch('http://localhost:8080/api/solicitudes/mis-pendientes', { headers });

        if (resProyectos.ok && resGrupos.ok) {
          setProyectos(await resProyectos.json());
          setGrupos(await resGrupos.json());
          if(resSolicitudes.ok) setSolicitudes(await resSolicitudes.json());
        } else {
          setError('No se pudieron cargar los datos.');
        }
      } catch (err) {
        setError('Error al conectar con el servidor.');
      } finally {
        setCargando(false);
      }
    };
    
    cargarDatos();
  }, [token]);

  const handleResponderSolicitud = async (solicitudId, accion) => {
    try {
      const res = await fetch(`http://localhost:8080/api/solicitudes/${solicitudId}/responder?accion=${accion}`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` }
      });
      
      if (res.ok) {
        setSolicitudes(solicitudes.filter(s => s.id !== solicitudId));
        alert(accion === 'aceptar' ? "¡Solicitud aceptada! Nuevo integrante añadido." : "Solicitud rechazada.");
      } else {
        const errorText = await res.text();
        alert("El servidor rechazó la acción: " + errorText);
      }
    } catch (err) { 
      alert("Error de conexión al procesar la solicitud."); 
    }
  };

  const verIntegrantes = async (id, tipo) => {
    const clave = `${tipo}-${id}`;
    if (integrantesVisibles === clave) {
      setIntegrantesVisibles(null);
      return;
    }
    
    try {
      const url = tipo === 'proyecto' 
        ? `http://localhost:8080/api/proyectos/${id}/integrantes` 
        : `http://localhost:8080/api/grupos-estudio/${id}/integrantes`;
        
      const res = await fetch(url, { headers: { 'Authorization': `Bearer ${token}` } });
      if (res.ok) {
        setListaIntegrantes(await res.json());
        setIntegrantesVisibles(clave);
      }
    } catch (err) { alert("Error al cargar integrantes."); }
  };

  const handleEliminarProyecto = async (id) => {
    if (window.confirm("¿Seguro que deseas eliminar este proyecto?")) {
      try {
        const res = await fetch(`http://localhost:8080/api/proyectos/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
        if (res.ok) setProyectos(proyectos.filter(p => p.id !== id));
      } catch (err) {}
    }
  };

  const handleEliminarGrupo = async (id) => {
    if (window.confirm("¿Seguro que deseas eliminar este grupo?")) {
      try {
        const res = await fetch(`http://localhost:8080/api/grupos-estudio/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
        if (res.ok) setGrupos(grupos.filter(g => g.id !== id));
      } catch (err) {}
    }
  };

  if (cargando) return <div style={{ textAlign: 'center', marginTop: '50px' }}>Cargando panel...</div>;

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>Mi Centro de Estudio</h2>
      {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}

      {solicitudes.length > 0 && (
        <div style={styles.solicitudesContainer}>
          <h3 style={styles.solicitudTitle}>🛎️ Tienes Solicitudes Pendientes</h3>
          {solicitudes.map(solicitud => (
            <div key={solicitud.id} style={styles.solicitudCard}>
              <p style={{margin: 0}}>
                <strong>{solicitud.nombreRemitente}</strong> quiere unirse a tu {solicitud.tipo}: <strong>{solicitud.tituloDestino}</strong>
              </p>
              <div>
                <button onClick={() => handleResponderSolicitud(solicitud.id, 'aceptar')} style={styles.btnAceptar}>Aceptar</button>
                <button onClick={() => handleResponderSolicitud(solicitud.id, 'rechazar')} style={styles.btnRechazar}>Rechazar</button>
              </div>
            </div>
          ))}
        </div>
      )}

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
                <p style={styles.docenteText}>Docente: {proyecto.nombreDocente}</p>
                <p style={styles.descText}>{proyecto.descripcion}</p>
                <p style={styles.dateText}><strong>Límite:</strong> {proyecto.fechaLimite}</p>
                
                <div style={styles.actionButtons}>
                  <button onClick={() => verIntegrantes(proyecto.id, 'proyecto')} style={styles.btnIntegrantes}>
                    {integrantesVisibles === `proyecto-${proyecto.id}` ? 'Ocultar Integrantes' : 'Ver Integrantes'}
                  </button>
                </div>

                {integrantesVisibles === `proyecto-${proyecto.id}` && (
                  <div style={styles.integrantesBox}>
                    <strong style={{fontSize: '13px'}}>Equipo:</strong>
                    <ul style={styles.lista}>
                      {listaIntegrantes.length > 0 
                        ? listaIntegrantes.map((nombre, i) => <li key={i}>{nombre}</li>)
                        : <li>Eres el único integrante</li>}
                    </ul>
                  </div>
                )}

                {proyecto.emailCreador === miEmail && (
                  <button onClick={() => handleEliminarProyecto(proyecto.id)} style={styles.deleteButton}>Eliminar</button>
                )}
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
                <p style={styles.docenteText}>Docente: {grupo.nombreDocente}</p>
                <p style={styles.descText}>{grupo.descripcion}</p>
                <p style={styles.dateText}><strong>Horario:</strong> {grupo.horarioHabitual}</p>
                
                <div style={styles.actionButtons}>
                  <button onClick={() => verIntegrantes(grupo.id, 'grupo')} style={styles.btnIntegrantes}>
                    {integrantesVisibles === `grupo-${grupo.id}` ? 'Ocultar Integrantes' : ' Ver Integrantes'}
                  </button>
                </div>

                {integrantesVisibles === `grupo-${grupo.id}` && (
                  <div style={styles.integrantesBox}>
                    <strong style={{fontSize: '13px'}}>Equipo:</strong>
                    <ul style={styles.lista}>
                      {listaIntegrantes.length > 0 
                        ? listaIntegrantes.map((nombre, i) => <li key={i}>{nombre}</li>)
                        : <li>Eres el único integrante</li>}
                    </ul>
                  </div>
                )}
                {grupo.emailCreador === miEmail && (
                  <button onClick={() => handleEliminarGrupo(grupo.id)} style={styles.deleteButton}>Eliminar</button>
                )}
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
  title: { textAlign: 'center', marginBottom: '20px', color: '#333' },
  grid: { display: 'flex', gap: '20px', justifyContent: 'space-between' },
  column: { flex: 1, background: '#f8f9fa', padding: '20px', borderRadius: '8px', border: '1px solid #dee2e6' },
  columnTitle: { marginBottom: '15px', borderBottom: '2px solid #ccc', paddingBottom: '5px' },
  card: { background: 'white', padding: '15px', borderRadius: '6px', marginBottom: '15px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)', border: '1px solid #e9ecef', position: 'relative' },
  materiaText: { fontSize: '13px', color: '#0056b3', fontWeight: 'bold', margin: '5px 0' },
  docenteText: { fontSize: '12px', color: '#6c757d', marginBottom: '5px', fontStyle: 'italic' },
  descText: { fontSize: '14px', color: '#555', marginBottom: '10px' },
  dateText: { fontSize: '13px', color: '#28a745' },
  emptyText: { fontSize: '14px', color: '#666', fontStyle: 'italic' },
  deleteButton: { position: 'absolute', top: '10px', right: '10px', background: '#dc3545', color: 'white', border: 'none', borderRadius: '4px', padding: '6px 10px', fontSize: '12px', cursor: 'pointer', fontWeight: 'bold' },
  solicitudesContainer: { background: '#fff3cd', border: '1px solid #ffe69c', padding: '15px', borderRadius: '8px', marginBottom: '20px' },
  solicitudTitle: { margin: '0 0 10px 0', fontSize: '16px', color: '#664d03' },
  solicitudCard: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', background: 'white', padding: '10px', borderRadius: '4px', border: '1px solid #ffda6a', marginBottom: '5px' },
  btnAceptar: { background: '#198754', color: 'white', border: 'none', padding: '5px 10px', borderRadius: '4px', cursor: 'pointer', marginRight: '5px' },
  btnRechazar: { background: '#dc3545', color: 'white', border: 'none', padding: '5px 10px', borderRadius: '4px', cursor: 'pointer' },
  actionButtons: { marginTop: '10px', paddingTop: '10px', borderTop: '1px dashed #ccc' },
  btnIntegrantes: { background: '#f8f9fa', border: '1px solid #ced4da', padding: '6px 12px', borderRadius: '4px', cursor: 'pointer', fontSize: '13px', width: '100%', color: '#495057', fontWeight: 'bold' },
  integrantesBox: { background: '#e9ecef', padding: '10px', borderRadius: '4px', marginTop: '10px' },
  lista: { margin: '5px 0 0 0', paddingLeft: '20px', fontSize: '13px', color: '#333' }
};