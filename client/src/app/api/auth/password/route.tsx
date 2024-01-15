import axios from "axios";

const uri = 'http://localhost:8080';

export const POST = async (email : string) => {
    try {
        const response = await axios.post(`${uri}/auth/password/find`,
        {
            email: email
        },
        {
            headers: { 'Content-Type': 'application/json' },
            withCredentials: true,
        });
        return {
            statusCode: 200,
            message: response.data.message
        }
    } catch (error : any) {
        const statusCode = error.response.status;
        const statusText = error.response.data.code;
        const message = error.response.data.message;
        return {
            statusCode: statusCode,
            statusText: statusText,
            message: message
        }
    }
}