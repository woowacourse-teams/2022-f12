import React from 'react';

import * as S from './Masonry.style';

type Props = { columnCount: number; children: React.ReactNode };

function Masonry({ columnCount, children }: Props) {
  const columns = Array.from({ length: columnCount }).reduce(
    (acc) => [...acc, []],
    []
  );

  React.Children.forEach(children, (child: React.ReactNode, index) => {
    columns[index % columnCount].push(child);
  });

  const childColumns = columns.map((column: React.ReactNode[], index) => {
    return <S.Column key={index}>{column}</S.Column>;
  });

  return <S.Container>{childColumns}</S.Container>;
}

export default Masonry;
