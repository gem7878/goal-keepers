'use client';

import React from 'react';
import { useTable } from 'react-table';

const Table = ({ columns, data, setSelectData, setOpen }: any) => {
  const { getTableProps, getTableBodyProps, headerGroups, rows, prepareRow } =
    useTable({ columns, data });
  return (
    <table
      {...getTableProps()}
      style={{ borderCollapse: 'collapse', width: '100%' }}
    >
      <thead>
        {headerGroups.map((headerGroup, index) => (
          <tr {...headerGroup.getHeaderGroupProps()} key={index}>
            {headerGroup.headers.map((column, i) => (
              <th
                {...column.getHeaderProps()}
                style={{
                  borderBottom: '1px solid #ddd',
                  background: '#f2f2f2',
                  padding: '8px',
                  textAlign: 'center',
                }}
                key={i}
              >
                {column.render('Header')}
              </th>
            ))}
          </tr>
        ))}
      </thead>
      <tbody {...getTableBodyProps()}>
        {rows.map((row, index) => {
          prepareRow(row);
          return (
            <tr
              {...row.getRowProps()}
              style={{ borderBottom: '1px solid #ddd' }}
              key={index}
              onClick={() => {
                setSelectData(row.original);
                setOpen(true);
              }}
            >
              {row.cells.map((cell, i) => (
                <td
                  {...cell.getCellProps()}
                  style={{ padding: '8px', textAlign: 'center' }}
                  key={i}
                >
                  {cell.render('Cell')}
                </td>
              ))}
            </tr>
          );
        })}
      </tbody>
    </table>
  );
};

export default Table;
