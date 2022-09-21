import Empty from '@/assets/empty.svg';

function NoDataPlaceholder() {
  return (
    <div>
      <Empty />
      <div style={{ width: '100%', textAlign: 'center' }}>아무것도 찾지 못했어요..</div>
    </div>
  );
}

export default NoDataPlaceholder;
