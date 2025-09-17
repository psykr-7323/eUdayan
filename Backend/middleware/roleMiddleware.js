const { errorResponse } = require("../utils/response");

const authorizeRoles = (...roles) => {
  return (req, res, next) => {
    if (!req.user) {
      return errorResponse(res, "Not authorized: no user found", 401);
    }

    if (!roles.includes(req.user.role)) {
      return errorResponse(res, "Access denied: insufficient permissions", 403);
    }

    next();
  };
};

module.exports = authorizeRoles;
