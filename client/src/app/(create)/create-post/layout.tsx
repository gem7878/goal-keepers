'use client';

import React from 'react';
const CreatePostLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <section className="gk-primary-create-section">
      <div className="h-full w-full flex flex-col justify-between">
        {children}
      </div>
    </section>
  );
};

export default CreatePostLayout;
