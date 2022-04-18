for (( i=0; i<=79; ++i));
do
    echo -n $i \-\>' ';
    java lang/lpse/LPseParser data/lpse/parser-tests/test$i.lpse;
done