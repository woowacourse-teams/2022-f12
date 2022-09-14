import React, { SVGProps } from 'react';

export const ReactComponent = 'div';
const mock = (props) => <svg {...props} />;
const SvgrMock = React.forwardRef<SVGSVGElement, SVGProps<SVGSVGElement>>(mock);

export default SvgrMock;
