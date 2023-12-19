import { NextRequest, NextResponse } from "next/server";

export const middleware = (request: NextRequest) => {
  // const excludeURL = [
  //   "/",
  //   "/create-goal/:path*",
  //   "/community",
  //   "/login",
  //   "/forgot-password",
  //   "/register",
  //   "social-register",
  //   "/create-post/select-goal",
  //   "/create-post/write-post",
  //   "/game",
  //   "/goal-list",
  //   "/my-page",
  // ];
  // if (excludeURL.includes(request.url)) {
  //   return NextResponse.next();
  // }
  // return NextResponse.redirect(new URL("/", request.url));
};
