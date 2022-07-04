import React from 'react';
import { Link } from 'react-router-dom';

type Props = {
  to: string;
  children: React.ReactNode;
};

function CustomLink({ to, children }: Props) {
  return <Link to={to}>{children}</Link>;
}

export default CustomLink;
