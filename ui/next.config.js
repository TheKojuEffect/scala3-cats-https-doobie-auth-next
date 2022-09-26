/** @type {import('next').NextConfig} */
module.exports = {
    reactStrictMode: true,
    rewrites() {
        return [
            {
                // Separate rewrite config for /api/login so that the cookie is set with path = '/' instead of path = '/api'
                source: '/login',
                destination: `${process.env.API_URL}/api/login`,
                basePath: false
            },
            {
                // Separate rewrite config for /api/logout to remove auth cookie with path = '/'
                source: '/logout',
                destination: `${process.env.API_URL}/api/logout`,
                basePath: false
            },
            {
                source: '/api/:path*',
                destination: `${process.env.API_URL}/api/:path*`,
                basePath: false
            }
        ]
    }
};
