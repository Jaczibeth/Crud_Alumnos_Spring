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

// Cargar alumnos desde la API
const cargarAlumnos = async () => {
  try {
    const response = await axios.get('http://localhost:8082/alumnos/traer-alumnos');
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
      await axios.put(`http://localhost:8082/alumnos/editar-alumnos/${nuevoAlumno.value.id}`, nuevoAlumno.value);
      Swal.fire({
        icon: 'success',
        title: 'Alumno actualizado',
        showConfirmButton: false,
        timer: 1500
      });
    } else {
      await axios.post('http://localhost:8082/alumnos/insertar-alumnos', nuevoAlumno.value);
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
    console.error('Error al guardar alumno:', error);
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
    await axios.delete(`http://localhost:8082/alumnos/eliminar-alumnos/${id}`);
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
              <div class="col-md-6 mb-3">
                <label for="numeroControl" class="form-label">Número de Control</label>
                <input type="number" class="form-control" id="numeroControl" v-model="nuevoAlumno.numeroControl" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="nombre" class="form-label">Nombre</label>
                <input type="text" class="form-control" id="nombre" v-model="nuevoAlumno.nombre" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="apellidos" class="form-label">Apellidos</label>
                <input type="text" class="form-control" id="apellidos" v-model="nuevoAlumno.apellidos" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="telefono" class="form-label">Teléfono</label>
                <input type="number" class="form-control" id="telefono" v-model="nuevoAlumno.telefono" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" v-model="nuevoAlumno.email" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="carrera" class="form-label">Carrera</label>
                <input type="text" class="form-control" id="carrera" v-model="nuevoAlumno.carrera" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="imagenURL" class="form-label">Imagen URL</label>
                <input type="text" class="form-control" id="imagenURL" v-model="nuevoAlumno.imagenURL">
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
  object-fit: cover;
  border-radius: 4px;
}

</style>