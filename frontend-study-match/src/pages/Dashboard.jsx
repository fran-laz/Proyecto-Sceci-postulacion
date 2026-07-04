import { useState, useEffect } from 'react';

export default function Dashboard() {
  const [accion, setAccion] = useState(''); 
  const [tipo, setTipo] = useState('');
  const [carreraId, setCarreraId] = useState('');
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
          setMateriaId(''); 
          setGrupoMateriaId('');
          setListaGrupos([]);
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
          setGrupoMateriaId('');
        })
        .catch(err => console.error("Error cargando grupos:", err));
    }
  }, [materiaId, carreraId, token]);

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
          
          {/* PASO 1 */}
          <div style={styles.inputGroup}>
            <label>¿Qué deseas hacer?</label>
            <select style={styles.select} value={accion} onChange={(e) => setAccion(e.target.value)} required>
              <option value="">-- Selecciona una opción --</option>
              <option value="crear">Crear uno nuevo</option>
              <option value="unirse">Unirse a uno existente</option>
            </select>
          </div>

          {/* PASO 2 */}
          {accion && (
            <div style={styles.inputGroup}>
              <label>¿De qué tipo?</label>
              <select style={styles.select} value={tipo} onChange={(e) => setTipo(e.target.value)} required>
                <option value="">-- Selecciona un tipo --</option>
                <option value="proyecto">Proyecto de Clase</option>
                <option value="estudio">Grupo de Estudio</option>
              </select>
            </div>
          )}

          {/* PASO 3: Carrera (Usando tus IDs reales) */}
          {tipo && (
            <div style={styles.inputGroup}>
              <label>Selecciona tu Carrera:</label>
              <select style={styles.select} value={carreraId} onChange={(e) => setCarreraId(e.target.value)} required>
                <option value="">-- Selecciona una carrera --</option>
                <option value="1">Ingeniería Informática</option>
                <option value="2">Ingeniería de Sistemas</option>
              </select>
            </div>
          )}

          {/* PASO 4: Materias (Dinámico) */}
          {carreraId && (
            <div style={styles.inputGroup}>
              <label>Selecciona la Materia:</label>
              <select style={styles.select} value={materiaId} onChange={(e) => setMateriaId(e.target.value)} required>
                <option value="">-- Selecciona una materia --</option>
                {listaMaterias.map((m) => (
                  <option key={m.id} value={m.id}>
                    {m.nombre}
                  </option>
                ))}
              </select>
            </div>
          )}


          {materiaId && (
            <div style={styles.inputGroup}>
              <label>Selecciona el Grupo/Docente:</label>
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
                <label>Título:</label>
                <input type="text" style={styles.input} required
                  value={titulo} onChange={(e) => setTitulo(e.target.value)} />
              </div>

              <div style={styles.inputGroup}>
                <label>Descripción:</label>
                <textarea style={{ ...styles.input, height: '60px' }} required
                  value={descripcion} onChange={(e) => setDescripcion(e.target.value)} />
              </div>

              <div style={styles.inputGroup}>
                <label>Máximo de Integrantes:</label>
                <input type="number" min="2" max="10" style={styles.input} required
                  value={maximoIntegrantes} onChange={(e) => setMaximoIntegrantes(e.target.value)} />
              </div>

              {tipo === 'proyecto' && (
                <div style={styles.inputGroup}>
                  <label>Fecha Límite:</label>
                  <input type="date" style={styles.input} required
                    value={fechaLimite} onChange={(e) => setFechaLimite(e.target.value)} />
                </div>
              )}


              {tipo === 'estudio' && (
                <>
                  <div style={styles.inputGroup}>
                    <label>Modalidad:</label>
                    <select style={styles.select} required value={modalidad} onChange={(e) => setModalidad(e.target.value)}>
                      <option value="">-- Selecciona modalidad --</option>
                      <option value="Presencial">Presencial</option>
                      <option value="Virtual">Virtual</option>
                      <option value="Híbrida">Híbrida</option>
                    </select>
                  </div>
                  <div style={styles.inputGroup}>
                    <label>Horario Habitual:</label>
                    <input type="text" placeholder="Ej: Jueves a las 14:15" style={styles.input} required
                      value={horarioHabitual} onChange={(e) => setHorarioHabitual(e.target.value)} />
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
  card: { background: 'white', padding: '30px', borderRadius: '8px', boxShadow: '0 4px 12px rgba(0,0,0,0.1)', width: '500px' },
  form: { display: 'flex', flexDirection: 'column' },
  inputGroup: { marginBottom: '15px', display: 'flex', flexDirection: 'column' },
  select: { padding: '10px', marginTop: '5px', borderRadius: '4px', border: '1px solid #ccc', fontSize: '15px' },
  input: { padding: '10px', marginTop: '5px', borderRadius: '4px', border: '1px solid #ccc', fontSize: '15px' },
  finalBox: { background: '#f8f9fa', padding: '15px', borderRadius: '6px', border: '1px solid #e9ecef', marginBottom: '15px' },
  button: { padding: '12px', background: '#28a745', color: 'white', border: 'none', borderRadius: '4px', fontSize: '16px', cursor: 'pointer', fontWeight: 'bold' }
};