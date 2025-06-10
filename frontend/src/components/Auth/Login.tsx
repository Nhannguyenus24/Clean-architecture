import React, { useState } from 'react';
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  Link
} from '@mui/material';
import { authService } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';

interface LoginProps {
  onSwitchToRegister: () => void;
}

const Login: React.FC<LoginProps> = ({ onSwitchToRegister }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await authService.login({ email, password });
      if (response.status === 200) {
        login(response.data.token);
        navigate('/chat');
      } else {
        setError(response.message);
      }
    } catch (err: any) {
      setError(err.response?.data?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container component="main" maxWidth="sm">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Paper 
          elevation={0} 
          sx={{ 
            padding: 5, 
            width: '100%',
            borderRadius: '20px',
            border: '1px solid #e4e6ea',
            backgroundColor: '#ffffff'
          }}
        >
          <Box sx={{ textAlign: 'center', mb: 4 }}>
            <Typography variant="h3" sx={{ mb: 1, color: '#0084ff' }}>
              💬
            </Typography>
            <Typography component="h1" variant="h4" sx={{ fontWeight: 600, color: '#1c1e21' }}>
              Welcome Back
            </Typography>
            <Typography variant="body2" sx={{ color: '#8e9297', mt: 1 }}>
              Sign in to continue your conversations
            </Typography>
          </Box>
          
          {error && (
            <Alert 
              severity="error" 
              sx={{ 
                mb: 3,
                borderRadius: '12px',
                backgroundColor: '#fee',
                border: '1px solid #fdd'
              }}
            >
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              sx={{
                mb: 2,
                '& .MuiOutlinedInput-root': {
                  borderRadius: '12px',
                  backgroundColor: '#f8f9fa',
                  '&:hover fieldset': {
                    borderColor: '#0084ff'
                  },
                  '&.Mui-focused fieldset': {
                    borderColor: '#0084ff'
                  }
                }
              }}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              sx={{
                mb: 3,
                '& .MuiOutlinedInput-root': {
                  borderRadius: '12px',
                  backgroundColor: '#f8f9fa',
                  '&:hover fieldset': {
                    borderColor: '#0084ff'
                  },
                  '&.Mui-focused fieldset': {
                    borderColor: '#0084ff'
                  }
                }
              }}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              disabled={loading}
              sx={{ 
                mt: 2, 
                mb: 3,
                py: 1.5,
                borderRadius: '12px',
                backgroundColor: '#0084ff',
                fontSize: '16px',
                fontWeight: 600,
                textTransform: 'none',
                '&:hover': {
                  backgroundColor: '#0073e6'
                },
                '&:disabled': {
                  backgroundColor: '#e4e6ea'
                }
              }}
            >
              {loading ? 'Signing in...' : 'Sign In'}
            </Button>
            <Box textAlign="center">
              <Link
                component="button"
                variant="body2"
                onClick={(e) => {
                  e.preventDefault();
                  onSwitchToRegister();
                }}
                sx={{
                  color: '#0084ff',
                  textDecoration: 'none',
                  fontWeight: 500,
                  '&:hover': {
                    textDecoration: 'underline'
                  }
                }}
              >
                Don't have an account? Sign up now
              </Link>
            </Box>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default Login; 