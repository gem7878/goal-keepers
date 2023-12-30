'use server';

import { POST } from '@/app/api/member/nickname/route';

export const handleChangeNickname = async (nickname: string) => {
  const formData = {
    nickname: nickname,
  };
  return POST(formData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};
