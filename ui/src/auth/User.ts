type Role = "Normal" | "Admin";

export interface User {
    id: string;
    email: string;
    role: Role
}