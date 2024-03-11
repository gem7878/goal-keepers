import { cookies } from 'next/headers';

export const handleGetToken = () => {
  const cookieStore = cookies();
  let token: string | undefined = cookieStore.has('_vercel_jwt')
    ? cookieStore.get('_vercel_jwt')?.value
    : cookieStore.get('accessToken')?.value;
  return { cookieStore: cookieStore, token: token };
};
export const handleGetEventId = () => {
  const cookieStore = cookies();
  let eventId: string | undefined = cookieStore.get('eventId')?.value;
  return { cookieStore: cookieStore, eventId: eventId };
};
