import errorGif from '@/assets/error.gif';
import errorWebp from '@/assets/error.webp';

function ErrorPlaceholder() {
  return (
    <div
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '1rem',
      }}
    >
      <picture>
        <source srcSet={errorWebp} type="image/webP" />
        <img src={errorGif} alt="" width="150" />
      </picture>
      <div role="alert">오류가 발생했어요..</div>
    </div>
  );
}

export default ErrorPlaceholder;
