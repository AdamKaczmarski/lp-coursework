for (( i=0; i<=79; ++i));
do
    echo -n $i \-\>' ';
    java lang/lpfun/LPfunParser data/lpfun/parser-tests/test$i.lpfun;
done