const express = require('express');
const router = express.Router();
const axios = require('axios');

const API_BASE_URL = 'http://localhost:5000/api';

// This is a simple in-memory store for the JWT token for testing purposes.
// In a real app, you'd use sessions or localStorage.
let authToken = null;

const getAuthHeaders = () => {
  return authToken ? { headers: { Authorization: `Bearer ${authToken}` } } : {};
};

// Home page
router.get('/', (req, res) => {
  res.render('index', { title: 'Backend Test Interface' });
});

// --- Auth Routes ---
router.get('/auth', (req, res) => {
  res.render('auth', { error: null, token: authToken });
});

router.post('/register', async (req, res) => {
  try {
    await axios.post(`${API_BASE_URL}/auth/signup`, req.body);
    res.redirect('/auth');
  } catch (error) {
    res.render('auth', { error: error.response?.data?.message || 'Registration failed', token: null });
  }
});

router.post('/login', async (req, res) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/login`, req.body);
    authToken = response.data.data.token;
    res.redirect('/auth');
  } catch (error) {
    res.render('auth', { error: error.response?.data?.message || 'Login failed', token: null });
  }
});

router.get('/logout', (req, res) => {
  authToken = null;
  res.redirect('/auth');
});

// --- Resources Routes ---
router.get('/resources', async (req, res) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/resources`);
    res.render('resources', { resources: response.data.data, error: null, token: authToken });
  } catch (error) {
    res.render('resources', { resources: [], error: 'Could not fetch resources.', token: authToken });
  }
});

router.post('/resources', async (req, res) => {
  if (!authToken) return res.redirect('/auth');
  try {
    await axios.post(`${API_BASE_URL}/resources`, req.body, getAuthHeaders());
    res.redirect('/resources');
  } catch (error) {
    const resourceRes = await axios.get(`${API_BASE_URL}/resources`);
    res.render('resources', {
      resources: resourceRes.data.data,
      error: error.response?.data?.message || 'Failed to add resource.',
      token: authToken
    });
  }
});

// --- Discussions Routes ---
router.get('/discussions', async (req, res) => {
  if (!authToken) return res.redirect('/auth');
  try {
    const response = await axios.get(`${API_BASE_URL}/discussion`, getAuthHeaders());
    res.render('discussions', { discussions: response.data.data, error: null, token: authToken });
  } catch (error) {
    res.render('discussions', { discussions: [], error: 'Could not fetch discussions.', token: authToken });
  }
});

router.post('/discussions', async (req, res) => {
  if (!authToken) return res.redirect('/auth');
  try {
    await axios.post(`${API_BASE_URL}/discussion`, req.body, getAuthHeaders());
    res.redirect('/discussions');
  } catch (error) {
    try {
      const discussionsRes = await axios.get(`${API_BASE_URL}/discussion`, getAuthHeaders());
      res.render('discussions', {
        discussions: discussionsRes.data.data,
        error: error.response?.data?.message || 'Failed to create discussion.',
        token: authToken
      });
    } catch (fetchError) {
       res.render('discussions', {
        discussions: [],
        error: 'Failed to create discussion and could not refetch list.',
        token: authToken
      });
    }
  }
});

// --- Chat Routes ---
router.get('/chat', async (req, res) => {
  if (!authToken) return res.redirect('/auth');
  try {
    // Fetch messages with the bot
    const response = await axios.get(`${API_BASE_URL}/chat/bot`, getAuthHeaders());
    res.render('chat', { messages: response.data.data, error: null, token: authToken });
  } catch (error) {
    res.render('chat', { messages: [], error: 'Could not fetch chat history.', token: authToken });
  }
});

router.post('/chat', async (req, res) => {
  if (!authToken) return res.redirect('/auth');
  try {
    // Send message to the bot (receiverId is null)
    await axios.post(`${API_BASE_URL}/chat`, { message: req.body.message }, getAuthHeaders());
    res.redirect('/chat');
  } catch (error) {
    res.render('chat', {
      messages: [], // Or refetch messages
      error: error.response?.data?.message || 'Failed to send message.',
      token: authToken
    });
  }
});

module.exports = router;