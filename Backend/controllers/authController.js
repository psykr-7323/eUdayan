const User = require("../models/user.js");
const jwt = require("jsonwebtoken");
const { successResponse, errorResponse } = require("../utils/response.js");

// Generate JWT
const generateToken = (id, role) => {
  return jwt.sign({ id, role }, process.env.JWT_SECRET, {
    expiresIn: "7d",
  });
};

// @desc Register user
exports.register = async (req, res) => {
  try {
    const { name, email, password, role, schoolInfo } = req.body;

    const userExists = await User.findOne({ email });
    if (userExists) {
      return errorResponse(res, "User already exists", 400);
    }

    const user = await User.create({ name, email, password, role, schoolInfo });
    const token = generateToken(user._id, user.role);

    const userResponse = { _id: user._id, name: user.name, email: user.email, role: user.role, token };

    return successResponse(res, "User registered successfully", userResponse, 201);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Registration failed");
  }
};

// @desc Login user
exports.login = async (req, res) => {
  try {
    const { email, password } = req.body;

    const user = await User.findOne({ email });
    if (!user || !(await user.matchPassword(password))) {
      return errorResponse(res, "Invalid credentials", 401);
    }

    const token = generateToken(user._id, user.role);
    const userResponse = {
      _id: user._id,
      name: user.name,
      email: user.email,
      role: user.role,
      token,
    };

    return successResponse(res, "Login successful", userResponse);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Login failed");
  }
};

// @desc Get profile
exports.getProfile = async (req, res) => {
  // The user object is already attached to the request by the authMiddleware,
  // and the password field is already excluded.
  return successResponse(res, "Profile fetched successfully", req.user);
};
