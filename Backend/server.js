const express = require('express');
const dotenv = require('dotenv');
const path = require('path');
const connectDB = require('./config/db');
const { notFound, errorHandler } = require('./middleware/errorMiddleware.js');

// Load env vars
dotenv.config();

// Connect to database
connectDB();

// Route files
const apiRoutes = require('./routes/index.js');
const viewRoutes = require('./routes/viewRoutes.js');

const app = express();

// Body parser
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// View Engine Setup
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
// A simple root route to check if the API is running
app.get('/', (req, res) => {
  res.send('eUdayan API is running...');
});

// Mount routers
app.use('/api', apiRoutes); // Your existing API will now be at /api
app.use('/', viewRoutes);   // The new test frontend will be at the root

// Error Middleware
app.use(notFound);
app.use(errorHandler);
const PORT = process.env.PORT || 3001;

const server = app.listen(
  PORT,
  console.log(`✅ Server running on port ${PORT}`)
);

// Handle unhandled promise rejections
process.on('unhandledRejection', (err, promise) => {
  console.log(`Error: ${err.message}`);
  server.close(() => process.exit(1));
});