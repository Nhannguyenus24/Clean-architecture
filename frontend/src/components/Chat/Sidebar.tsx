import React from 'react';
import {
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Typography,
  Box,
  Button,
  Divider
} from '@mui/material';
import {
  Add as AddIcon,
  Chat as ChatIcon,
  Logout as LogoutIcon
} from '@mui/icons-material';
import { Conversation } from '../../types';
import { useAuth } from '../../context/AuthContext';

interface SidebarProps {
  conversations: Conversation[];
  currentConversationId: number | null;
  onConversationSelect: (conversationId: number) => void;
  onNewChat: () => void;
}

const SIDEBAR_WIDTH = 280;

const Sidebar: React.FC<SidebarProps> = ({
  conversations,
  currentConversationId,
  onConversationSelect,
  onNewChat
}) => {
  const { logout } = useAuth();

  const handleNewChat = () => {
    onNewChat(); // Just call the parent handler
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - date.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 1) {
      return 'Today';
    } else if (diffDays === 2) {
      return 'Yesterday';
    } else if (diffDays <= 7) {
      return `${diffDays - 1} days ago`;
    } else {
      return date.toLocaleDateString('en-US');
    }
  };

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: SIDEBAR_WIDTH,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: SIDEBAR_WIDTH,
          boxSizing: 'border-box',
          backgroundColor: '#202123',
          color: 'white',
          borderRight: '1px solid #2d2d30'
        },
      }}
    >
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
        {/* New Chat Button */}
        <Box sx={{ p: 3, pb: 2 }}>
          <Button
            fullWidth
            variant="outlined"
            startIcon={<AddIcon />}
            onClick={handleNewChat}
            sx={{
              color: 'white',
              borderColor: '#40414f',
              backgroundColor: 'transparent',
              borderRadius: '12px',
              py: 1.5,
              fontSize: '14px',
              fontWeight: 500,
              textTransform: 'none',
              '&:hover': {
                backgroundColor: '#40414f',
                borderColor: '#565869'
              }
            }}
          >
            New Chat
          </Button>
        </Box>

        {/* Chat History */}
        <Box sx={{ flex: 1, overflow: 'auto', px: 2 }}>
          <List sx={{ p: 0 }}>
            {conversations.map((conversation) => (
              <ListItem key={conversation.id} disablePadding sx={{ mb: 1 }}>
                <ListItemButton
                  selected={currentConversationId === conversation.id}
                  onClick={() => onConversationSelect(conversation.id)}
                  sx={{
                    px: 3,
                    py: 2,
                    borderRadius: '12px',
                    transition: 'all 0.2s ease',
                    '&.Mui-selected': {
                      backgroundColor: '#343541',
                      '&:hover': {
                        backgroundColor: '#40414f'
                      }
                    },
                    '&:hover': {
                      backgroundColor: '#2a2b32'
                    }
                  }}
                >
                  <ChatIcon sx={{ mr: 3, fontSize: 18, color: '#8e9297' }} />
                  <ListItemText
                    primary={
                      <Typography
                        variant="body2"
                        sx={{
                          fontWeight: currentConversationId === conversation.id ? 600 : 400,
                          overflow: 'hidden',
                          textOverflow: 'ellipsis',
                          whiteSpace: 'nowrap',
                          fontSize: '14px',
                          color: currentConversationId === conversation.id ? '#ffffff' : '#d1d5db'
                        }}
                      >
                        {conversation.name}
                      </Typography>
                    }
                    secondary={
                      <Typography 
                        variant="caption" 
                        sx={{ 
                          color: '#8e9297',
                          fontSize: '12px'
                        }}
                      >
                        {formatDate(conversation.createdAt)}
                      </Typography>
                    }
                  />
                </ListItemButton>
              </ListItem>
            ))}
            {conversations.length === 0 && (
              <Box sx={{ p: 3, textAlign: 'center' }}>
                <Typography sx={{ color: '#8e9297', fontSize: '14px', mb: 1 }}>
                  No conversations yet
                </Typography>
                <Typography sx={{ color: '#6f7177', fontSize: '12px' }}>
                  Start a new chat to begin
                </Typography>
              </Box>
            )}
          </List>
        </Box>

        <Divider sx={{ borderColor: '#2d2d30', mx: 2 }} />

        {/* Logout Button */}
        <Box sx={{ p: 3, pt: 2 }}>
          <Button
            fullWidth
            variant="text"
            startIcon={<LogoutIcon />}
            onClick={logout}
            sx={{
              color: '#8e9297',
              borderRadius: '12px',
              py: 1.5,
              fontSize: '14px',
              fontWeight: 500,
              textTransform: 'none',
              '&:hover': {
                backgroundColor: '#40414f',
                color: 'white'
              }
            }}
          >
            Logout
          </Button>
        </Box>
      </Box>
    </Drawer>
  );
};

export default Sidebar; 