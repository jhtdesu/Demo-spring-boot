import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import api from './api/axiosConfig'
import { useState, useEffect } from 'react'

function App() {
  const [users, setUsers] = useState([])

  useEffect(() => { 
    loadUsers()
  });

  const loadUsers = async () => {
    const result = await api.get('http://localhost:8080/getAllUsers')
    console.log(result.data)
}
}
export default App
