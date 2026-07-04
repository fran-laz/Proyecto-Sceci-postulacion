import { useState, useEffect } from 'react';

export default function Dashboard() {
  const [accion, setAccion] = useState(''); 
  const [tipo, setTipo] = useState('');
  const [carreraId, setCarreraId] = useState('');
  const [semestreSeleccionado, setSemestreSeleccionado] = useState('');
  const [materiaId, setMateriaId] = useState('');
  const [grupoMateriaId, setGrupoMateriaId] = useState('');

  const [listaMaterias, setListaMaterias] = useState([]);
  const [listaGrupos, setListaGrupos] = useState([]);
  const [titulo, setTitulo] = useState('');
  const [descripcion, setDescripcion] = useState('');
  const [maximoIntegrantes, setMaximoIntegrantes] = useState('');

  const [fechaLimite, setFechaLimite] = useState('');

  const [modalidad, setModalidad] = useState('');
  const [horarioHabitual, setHorarioHabitual] = useState('');

  const token = localStorage.getItem('token');

  useEffect(() => {
    if (carreraId) {
      fetch(`http://localhost:8080/api/materias?carreraId=${carreraId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
        .then(res => res.json())
        .then(data => {
          setListaMaterias(data);
        })
        .catch(err => console.error("Error cargando materias:", err));
    }
  }, [carreraId, token]);

  useEffect(() => {
    if (materiaId && carreraId) {
      fetch(`http://localhost:8080/api/materias/${materiaId}/grupos?carreraId=${carreraId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
        .then(res => res.json())
        .then(data => {
          setListaGrupos(data);
        })
        .catch(err => console.error("Error cargando grupos:", err));
    }
  }, [materiaId, carreraId, token]);

  const handleTipoChange = (nuevoTipo) => {
    setTipo(nuevoTipo);
    setCarreraId('');
    setSemestreSeleccionado('');
    setMateriaId('');
    setGrupoMateriaId('');
    setListaGrupos([]);
  };

  const handleCarreraChange = (id) => {
    setCarreraId(id);
    setSemestreSeleccionado('');
    setMateriaId('');
    setGrupoMateriaId('');
    setListaGrupos([]);
  };

  const handleSemestreChange = (semestre) => {
    setSemestreSeleccionado(semestre);
    setMateriaId('');
    setGrupoMateriaId('');
    setListaGrupos([]);
  };

  const handleMateriaChange = (id) => {
    setMateriaId(id);
    setGrupoMateriaId('');
  };

  const semestresUnicos = [...new Set(listaMaterias.map(m => m.semestre))].filter(Boolean);
  const materiasFiltradas = listaMaterias.filter(m => m.semestre === semestreSeleccionado);

  const handleSubmit = async (e) => {
    e.preventDefault();

    let url = '';
    let bodyData = {
      titulo,
      descripcion,
      maximoIntegrantes: parseInt(maximoIntegrantes),
      grupoMateriaId: parseInt(grupoMateriaId)
    };

    if (tipo === 'proyecto') {
      url = 'http://localhost:8080/api/proyectos';
      bodyData.fechaLimite = fechaLimite;
    } else {
      url = 'http://localhost:8080/api/grupos-estudio';
      bodyData.modalidad = modalidad;
      bodyData.horarioHabitual = horarioHabitual;
    }

    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(bodyData)
      });

      if (response.ok) {
        alert(`¡${tipo === 'proyecto' ? 'Proyecto' : 'Grupo de Estudio'} creado exitosamente!`);
      } else {
        alert("Ocurrió un error al crear. Revisa los datos.");
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("Error de conexión con el servidor.");
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Panel Principal</h2>

        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>1. ¿Qué deseas hacer?</label>
            <div style={styles.buttonGrid}>
              <button 
                type="button" 
                onClick={() => setAccion('crear')} 
                style={accion === 'crear' ? styles.buttonActive : styles.buttonInactive}
              >
                Crear uno nuevo
              </button>
              <button 
                type="button" 
                onClick={() => setAccion('unirse')} 
                style={accion === 'unirse' ? styles.buttonActive : styles.buttonInactive}
              >
                Unirse a uno existente
              </button>
            </div>
          </div>

          {accion && (
            <div style={styles.inputGroup}>
              <label style={styles.label}>2. ¿De qué tipo?</label>
              <div style={styles.buttonGrid}>
                <button 
                  type="button" 
                  onClick={() => handleTipoChange('proyecto')} 
                  style={tipo === 'proyecto' ? styles.buttonActive : styles.buttonInactive}
                >
                  Proyecto de Clase
                </button>
                <button 
                  type="button" 
                  onClick={() => handleTipoChange('estudio')} 
                  style={tipo === 'estudio' ? styles.buttonActive : styles.buttonInactive}
                >
                  Grupo de Estudio
                </button>
              </div>
            </div>
          )}

          {tipo && (
            <div style={styles.inputGroup}>
              <label style={styles.label}>3. Selecciona tu Carrera:</label>
              <div style={styles.buttonGrid}>
                <button 
                  type="button" 
                  onClick={() => handleCarreraChange("1")} 
                  style={carreraId === "1" ? styles.buttonActive : styles.buttonInactive}
                >
                  Ingeniería Informática
                </button>
                <button 
                  type="button" 
                  onClick={() => handleCarreraChange("2")} 
                  style={carreraId === "2" ? styles.buttonActive : styles.buttonInactive}
                >
                  Ingeniería de Sistemas
                </button>
              </div>
            </div>
          )}

          {carreraId && listaMaterias.length > 0 && (
            <div style={styles.inputGroup}>
              <label style={styles.label}>Selecciona el Semestre:</label>
              <div style={styles.buttonGrid}>
                {semestresUnicos.map((semestre) => (
                  <button 
                    key={semestre} 
                    type="button" 
                    onClick={() => handleSemestreChange(semestre)} 
                    style={semestreSeleccionado === semestre ? styles.buttonActive : styles.buttonInactive}
                  >
                    Semestre {semestre}
                  </button>
                ))}
              </div>
            </div>
          )}

          {semestreSeleccionado && (
            <div style={styles.inputGroup}>
              <label style={styles.label}>Selecciona la Materia:</label>
              <div style={styles.buttonGrid}>
                {materiasFiltradas.map((m) => (
                  <button 
                    key={m.id} 
                    type="button" 
                    onClick={() => handleMateriaChange(m.id)} 
                    style={materiaId === m.id ? styles.buttonActive : styles.buttonInactive}
                  >
                    {m.nombre}
                  </button>
                ))}
              </div>
            </div>
          )}


          {materiaId && (
            <div style={styles.inputGroup}>
              <label style={styles.label}>Selecciona el Grupo/Docente:</label>
              <select style={styles.select} value={grupoMateriaId} onChange={(e) => setGrupoMateriaId(e.target.value)} required>
                <option value="">-- Selecciona un docente --</option>
                {listaGrupos.map((g) => (
                  <option key={g.id} value={g.id}>
                    Grupo {g.numeroGrupo} - {g.nombreDocente}
                  </option>
                ))}
              </select>
            </div>
          )}


          {grupoMateriaId && accion === 'crear' && (
            <div style={styles.finalBox}>
              <h4 style={{ marginBottom: '15px' }}>Detalles del {tipo === 'proyecto' ? 'Proyecto' : 'Grupo de Estudio'}</h4>
              
              <div style={styles.inputGroup}>
                <label style={styles.label}>Título:</label>
                <input type="text" style={styles.input} required value={titulo} onChange={(e) => setTitulo(e.target.value)} />
              </div>

              <div style={styles.inputGroup}>
                <label style={styles.label}>Descripción:</label>
                <textarea style={{ ...styles.input, height: '60px' }} required value={descripcion} onChange={(e) => setDescripcion(e.target.value)} />
              </div>

              <div style={styles.inputGroup}>
                <label style={styles.label}>Máximo de Integrantes:</label>
                <input type="number" min="2" max="10" style={styles.input} required value={maximoIntegrantes} onChange={(e) => setMaximoIntegrantes(e.target.value)} />
              </div>

              {tipo === 'proyecto' && (
                <div style={styles.inputGroup}>
                  <label style={styles.label}>Fecha Límite:</label>
                  <input type="date" style={styles.input} required value={fechaLimite} onChange={(e) => setFechaLimite(e.target.value)} />
                </div>
              )}

              {tipo === 'estudio' && (
                <>
                  <div style={styles.inputGroup}>
                    <label style={styles.label}>Modalidad:</label>
                    <select style={styles.select} required value={modalidad} onChange={(e) => setModalidad(e.target.value)}>
                      <option value="">-- Selecciona modalidad --</option>
                      <option value="Presencial">Presencial</option>
                      <option value="Virtual">Virtual</option>
                      <option value="Híbrida">Híbrida</option>
                    </select>
                  </div>
                  <div style={styles.inputGroup}>
                    <label style={styles.label}>Horario Habitual:</label>
                    <input type="text" placeholder="Ej: Jueves a las 14:15" style={styles.input} required value={horarioHabitual} onChange={(e) => setHorarioHabitual(e.target.value)} />
                  </div>
                </>
              )}
            </div>
          )}

          {grupoMateriaId && (
            <button type="submit" style={styles.button}>
              {accion === 'crear' ? 'Confirmar Creación' : 'Ver Disponibles'}
            </button>
          )}

        </form>
      </div>
    </div>
  );
}

const styles = {
  container: { display: 'flex', justifyContent: 'center', marginTop: '30px', paddingBottom: '50px' },
  card: { background: 'white', padding: '30px', borderRadius: '8px', boxShadow: '0 4px 12px rgba(0,0,0,0.1)', width: '550px' }, 
  form: { display: 'flex', flexDirection: 'column' },
  inputGroup: { marginBottom: '15px', display: 'flex', flexDirection: 'column' },
  label: { fontWeight: 'bold', color: '#444', marginBottom: '8px' }, 
  select: { padding: '10px', borderRadius: '4px', border: '1px solid #ccc', fontSize: '15px' },
  input: { padding: '10px', borderRadius: '4px', border: '1px solid #ccc', fontSize: '15px' },
  finalBox: { background: '#f8f9fa', padding: '15px', borderRadius: '6px', border: '1px solid #e9ecef', marginBottom: '15px', marginTop: '10px' },
  button: { padding: '12px', background: '#28a745', color: 'white', border: 'none', borderRadius: '4px', fontSize: '16px', cursor: 'pointer', fontWeight: 'bold' },
  
  buttonGrid: { display: 'flex', flexWrap: 'wrap', gap: '8px' },
  buttonInactive: { 
    padding: '8px 14px', 
    background: '#f8f9fa', 
    border: '1px solid #ced4da', 
    borderRadius: '6px', 
    cursor: 'pointer', 
    color: '#495057', 
    fontSize: '14px',
    transition: 'all 0.2s' 
  },
  buttonActive: { 
    padding: '8px 14px', 
    background: '#0d6efd',
    border: '1px solid #0d6efd', 
    borderRadius: '6px', 
    cursor: 'pointer', 
    color: 'white', 
    fontWeight: 'bold',
    fontSize: '14px',
    transition: 'all 0.2s', 
    boxShadow: '0 2px 4px rgba(13, 110, 253, 0.3)' 
  }
};