<!-- Agrega esto en public/index.html o en tu archivo HTML base -->
<!-- CDN de Bootstrap Icons para evitar errores de carga -->
<!-- Puedes ignorar esto si ya lo agregaste -->
<!--
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
-->

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

const alumnos = ref([]);

const nuevoAlumno = ref({
  id: null,
  numeroControl: '',
  nombre: '',
  apellidos: '',
  telefono: '',
  email: '',
  carrera: '',
  imagenURL: ''
});

const editado = ref(false);

// Dirección IP común para todas las peticiones
const API_URL = 'http://localhost:8080/alumnos';

// Cargar alumnos desde la API
const cargarAlumnos = async () => {
  try {
    const response = await axios.get(`${API_URL}/traer-alumnos`);
    alumnos.value = response.data;
  } catch (error) {
    console.error('Error al cargar alumnos:', error);
    Swal.fire('Error', 'No se pudieron cargar los alumnos.', 'error');
  }
};

// Agregar o actualizar alumno
const agregarAlumno = async () => {
  try {
    if (editado.value) {
      await axios.put(`${API_URL}/editar-alumnos/${nuevoAlumno.value.id}`, nuevoAlumno.value);
      Swal.fire({
        icon: 'success',
        title: 'Alumno actualizado',
        showConfirmButton: false,
        timer: 1500
      });
    } else {
      await axios.post(`${API_URL}/insertar-alumnos`, nuevoAlumno.value);
      Swal.fire({
        icon: 'success',
        title: 'Alumno agregado',
        showConfirmButton: false,
        timer: 1500
      });
    }

    await cargarAlumnos();
    nuevoAlumno.value = {
      id: null,
      numeroControl: '',
      nombre: '',
      apellidos: '',
      telefono: '',
      email: '',
      carrera: '',
      imagenURL: ''
    };
    editado.value = false;
  } catch (error) {
    console.error('Error al guardar alumno:', error.response?.data || error.message);
    Swal.fire('Error', 'Hubo un problema al guardar los datos del alumno.', 'error');
  }
};

// Cargar datos del alumno al formulario
const editarAlumno = (alumno) => {
  nuevoAlumno.value = { ...alumno };
  editado.value = true;
};

// Eliminar alumno con confirmación
const eliminarAlumno = async (id) => {
  const result = await Swal.fire({
    title: '¿Estás seguro?',
    text: "No podrás revertir esto",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Sí, eliminarlo'
  });

  if (result.isConfirmed) {
    await eliminarAlumnoPorId(id);
    Swal.fire('Eliminado', 'El alumno ha sido eliminado.', 'success');
  }
};

// Eliminar alumno por ID
const eliminarAlumnoPorId = async (id) => {
  try {
    await axios.delete(`${API_URL}/eliminar-alumnos/${id}`);
    await cargarAlumnos();
  } catch (error) {
    console.error('Error al eliminar alumno:', error);
    Swal.fire('Error', 'No se pudo eliminar el alumno.', 'error');
  }
};

onMounted(cargarAlumnos);
</script>

<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12 mt-4">
        <div class="card shadow p-4 mb-4">
          <h2 class="text-center">Formulario de Alumnos</h2>
          <form @submit.prevent="agregarAlumno">
            <div class="row">
              <div class="col-md-6 mb-3" v-for="(label, key) in {
                  numeroControl: 'Número de Control',
                  nombre: 'Nombre',
                  apellidos: 'Apellidos',
                  telefono: 'Teléfono',
                  email: 'Email',
                  carrera: 'Carrera',
                  imagenURL: 'Imagen URL'
                }" :key="key">
                <label :for="key" class="form-label">{{ label }}</label>
                <input
                  :type="key === 'email' ? 'email' : (key === 'telefono' || key === 'numeroControl' ? 'number' : 'text')"
                  class="form-control"
                  :id="key"
                  v-model="nuevoAlumno[key]"
                  :required="key !== 'imagenURL'"
                >
              </div>
            </div>
            <button type="submit" class="btn btn-primary">
              {{ editado ? 'Actualizar Alumno' : 'Agregar Alumno' }}
            </button>
          </form>
        </div>
      </div>

      <div class="col-md-12">
        <div class="card shadow">
          <div class="card-body">
            <h5 class="card-title mb-3">Tabla de Alumnos</h5>
            <table class="table table-hover align-middle">
              <thead class="table-dark">
                <tr>
                  <th>ID</th>
                  <th>Número Control</th>
                  <th>Nombre</th>
                  <th>Apellidos</th>
                  <th>Teléfono</th>
                  <th>Email</th>
                  <th>Carrera</th>
                  <th>Imagen</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="alumno in alumnos" :key="alumno.id">
                  <td>{{ alumno.id }}</td>
                  <td>{{ alumno.numeroControl }}</td>
                  <td>{{ alumno.nombre }}</td>
                  <td>{{ alumno.apellidos }}</td>
                  <td>{{ alumno.telefono }}</td>
                  <td>{{ alumno.email }}</td>
                  <td>{{ alumno.carrera }}</td>
                  <td>
                    <img
                      :src="alumno.imagenURL || 'https://via.placeholder.com/50'"
                      alt="Imagen de Alumno"
                      width="50"
                      height="50"
                    />
                  </td>
                  <td>
                    <button @click="eliminarAlumno(alumno.id)" class="btn btn-danger mx-1">
                      <i class="bi bi-trash2-fill"></i>
                    </button>
                    <button @click="editarAlumno(alumno)" class="btn btn-warning mx-1">
                      <i class="bi bi-pencil-fill"></i>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
img {
  border-radius: 5px;
}
</style>
