'use server';

import { setAccessTokenCookie } from '@/app/(auth)/login/actions';
import { POST as NicknamePOST } from '@/app/api/member/nickname/route';
import { POST as PasswordPOST } from '@/app/api/member/password/route';

export const handleChangeNickname = async (nickname: string) => {
  const formData = {
    nickname: nickname,
  };
  return NicknamePOST(formData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};
export const handleChangePassword = async (formData: {
  email: string;
  exPassword: string;
  newPassword: string;
}) => {
  return PasswordPOST(formData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        setAccessTokenCookie(JSON.parse(response.body).data.accessToken);
        return JSON.parse(response.body);
      } else if (response.statusCode === 400) {
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};
