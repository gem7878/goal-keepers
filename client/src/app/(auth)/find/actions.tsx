'use server';
import { POST } from '@/app/api/auth/password/route';

export const handleFindPassword = (postData: {
    email: string;
  }) => {
    return POST(postData.email);
}
