'use server';

import axios from 'axios';

export const kakaoLogin = (code: String | null) => {
  const geturl = `http://localhost:8080/api/kakao/login?code=${code}`;
  axios
    .get(geturl, {
      headers: {
        'Content-Type': 'application/json;charset=utf-8',
      },
    })
    .then((res) => {
      console.log('나오니?', res.data);
      if (res.status === 200) {
        console.log(res.data);
      }
    });
};
