'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import React, { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { selectRender } from '@/redux/renderSlice';

const CreatePostLayout = ({ children }: { children: React.ReactNode }) => {
  const pathname = usePathname();
  const reduxCreateButton = useSelector(selectRender);


  const handleCreatePost = async () => {
    if (pathname !== '/create-post/select-goal') {
      const postData = {
        goalId: reduxCreateButton.goalId,
      };
      await handleCreatePost();
    }
  };

  useEffect(() => {}, [reduxCreateButton]);
  return (
    <section className="gk-primary-create-section">
      <div className="h-full w-full flex flex-col justify-between">
        {children}
      </div>
      {/* <Link
        className={
          reduxCreateButton.goalId !== null
            ? 'gk-primary-next-a'
            : 'gk-primary-next-a-block'
        }
        href={{
          pathname:
            reduxCreateButton.goalId !== null
              ? pathname === '/create-post/select-goal'
                ? `/create-post/write-post/`
                : `/community`
              : '',
          query: { goalId: reduxCreateButton.goalId },
        }}
      >
        <button className="w-full h-full">다음</button>
      </Link> */}
    </section>
  );
};

export default CreatePostLayout;
