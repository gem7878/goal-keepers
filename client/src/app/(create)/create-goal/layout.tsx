'use client';

import Link from 'next/link';
import { notFound, useParams, useSearchParams } from 'next/navigation';
import React, { useEffect, useState } from 'react';

export default function CreateGoalLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const params = useSearchParams();
  const item = params.get('item');
  
  // const params = useParams<{ createGoalId: any }>();

  // console.log(params);

  // const [createGoalId, setCreateGoalId] = useState(1);

  // useEffect(() => {
  //   setCreateGoalId(parseInt(params.createGoalId));
  // }, [params.createGoalId]);

  const createGoalList = ['title', 'description', 'date', 'image'];

  // if (createGoalId > createGoalList.length) {
  //   notFound();
  // }
  return (
    <section className="gk-primary-create-section">
      <nav className="w-16">
        <ul className="flex px-1 border border-orange-300	justify-around rounded-md	items-center">
          {createGoalList.map((_, index: number) => {
            if (createGoalList[index] === item) {
              return (
                <li key={index} className="text-orange-300 text-xs	">
                  {index + 1}
                </li>
              );
            } else {
              return (
                <li key={index} className="leading-4	">
                  <div className="rounded-md w-2 h-2 bg-orange-100	"></div>
                </li>
              );
            }
          })}
        </ul>
      </nav>
      {children}
    </section>
  );
}
