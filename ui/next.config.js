/** @type {import('next').NextConfig} */
module.exports = {
    reactStrictMode: true,
    async rewrites() {
        return [
            {
                // Separate rewrite config for /api/login so that the cookie is set with path = '/' instead of path = '/api'
                source: '/login',
                destination: `${process.env.API_URL}/api/login`,
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
