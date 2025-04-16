import './App.css'
import api from './api/axiosConfig'
import { useState, useEffect } from 'react'

function App() {
  const [users, setUsers] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    loadUsers()
  }, [])

  const loadUsers = async () => {
    try {
      const result = await api.get('/getAllUsers')
      setUsers(result.data)
    } catch (err) {
      console.error("Lỗi khi tải dữ liệu người dùng:", err)
      setError("Không thể tải danh sách người dùng.")
    }
  }

  return (
    <div className="App">
      <h1>Danh sách người dùng</h1>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      {users.length > 0 ? (
        <table border="1" cellPadding="8" style={{ borderCollapse: 'collapse', margin: 'auto' }}>
          <thead>
            <tr>
              <th>#</th>
              <th>Tên</th>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user, index) => (
              <tr key={user.id || index}>
                <td>{index + 1}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Không có người dùng nào.</p>
      )}
    </div>
  )
}

export default App
