import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './UserProfile.css';
import Navbar from '../components/Navbar';

const UserProfile = () => {
  const [user, setUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editedUser, setEditedUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        // Get the current user's ID from localStorage
        const userId = localStorage.getItem('userId');
        if (!userId) {
          throw new Error('No user ID found');
        }
        const response = await axios.get(`http://localhost:8086/api/users/${userId}`, {
          withCredentials: true
        });
        setUser(response.data);
        setEditedUser(response.data);
        setLoading(false);
      } catch (err) {
        console.error('Failed to load user profile:', err);
        setError('Failed to load user profile');
        setLoading(false);
      }
    };

    fetchUserProfile();
  }, []);

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleSave = async () => {
    try {
      const response = await axios.put(
        `http://localhost:8086/api/users/${user.id}/profile`,
        editedUser,
        { withCredentials: true }
      );
      setUser(response.data);
      setIsEditing(false);
    } catch (err) {
      console.error('Failed to update profile:', err);
      setError('Failed to update profile');
    }
  };

  const handleCancel = () => {
    setEditedUser(user);
    setIsEditing(false);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedUser(prev => ({
      ...prev,
      [name]: value
    }));
  };

  if (loading) return (
    <>
      <Navbar />
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading profile...</p>
      </div>
    </>
  );

  if (error) return (
    <>
      <Navbar />
      <div className="error-container">
        <i className="error-icon">⚠️</i>
        {error}
      </div>
    </>
  );

  return (
    <>
      <Navbar />
      <div className="profile-container">
        <div className="profile-header">
          <h1>User Profile</h1>
          {user?.isModerator && (
            <span className="moderator-badge">Moderator</span>
          )}
        </div>

        <div className="profile-content">
          {isEditing ? (
            <div className="edit-form">
              <div className="form-group">
                <label>Name:</label>
                <input
                  type="text"
                  name="name"
                  value={editedUser.name}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <label>Email:</label>
                <input
                  type="email"
                  value={user.email}
                  disabled
                />
              </div>

              <div className="form-group">
                <label>Bio:</label>
                <textarea
                  name="bio"
                  value={editedUser.bio || ''}
                  onChange={handleChange}
                  rows="4"
                />
              </div>

              <div className="form-group">
                <label>Location:</label>
                <input
                  type="text"
                  name="location"
                  value={editedUser.location || ''}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <label>Profile Picture URL:</label>
                <input
                  type="text"
                  name="profilePicture"
                  value={editedUser.profilePicture || ''}
                  onChange={handleChange}
                />
              </div>

              <div className="button-group">
                <button onClick={handleSave} className="save-btn">Save Changes</button>
                <button onClick={handleCancel} className="cancel-btn">Cancel</button>
              </div>
            </div>
          ) : (
            <div className="profile-info">
              <div className="profile-picture">
                {user.profilePicture ? (
                  <img src={user.profilePicture} alt="Profile" />
                ) : (
                  <div className="default-avatar">
                    {user.name?.charAt(0).toUpperCase()}
                  </div>
                )}
              </div>

              <div className="info-group">
                <label>Name:</label>
                <p>{user.name}</p>
              </div>

              <div className="info-group">
                <label>Email:</label>
                <p>{user.email}</p>
              </div>

              <div className="info-group">
                <label>Bio:</label>
                <p>{user.bio || 'No bio yet'}</p>
              </div>

              <div className="info-group">
                <label>Location:</label>
                <p>{user.location || 'Not specified'}</p>
              </div>

              <div className="info-group">
                <label>Member Since:</label>
                <p>{new Date(user.joinDate).toLocaleDateString()}</p>
              </div>

              <button onClick={handleEdit} className="edit-btn">Edit Profile</button>
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default UserProfile; 