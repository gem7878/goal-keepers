'use client';
import React, { SetStateAction, useEffect, useRef, useState } from 'react';

interface selectDataTypes {
  index: number;
  title: string;
  description: string;
  date: string;
}

const CsModal: React.FC<{
  setOpen: React.Dispatch<SetStateAction<boolean>>;
  selectData: selectDataTypes | null;
}> = ({ setOpen, selectData }) => {
  const containerRef = useRef<HTMLElement>(null);

  const handleOutsideClick = (e: any) => {
    if (!containerRef.current?.contains(e.target)) {
      setOpen(false);
    }
  };
  return (
    <div
      className="fixed top-0 w-screen h-screen bg-black bg-opacity-70 flex items-center justify-center"
      onClick={(e) => handleOutsideClick(e)}
    >
      <main className="w-3/4 h-3/5 bg-white opacity-100 " ref={containerRef}>
        <section className="w-full h-1/5 relative">
          <h2>{selectData?.title}</h2>
          <h2>{selectData?.date}</h2>
        </section>
        <section className="h-1/5 w-full flex items-center justify-center gap-2">
          <h2>{selectData?.description}</h2>
        </section>
      </main>
    </div>
  );
};

export default CsModal;
