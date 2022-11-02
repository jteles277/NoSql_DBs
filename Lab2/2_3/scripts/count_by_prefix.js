function count_by_prefix(prefix) {
    var resultado =db.phones.aggregate([
        {$match: {"components.country": prefix}},
        {$group: {_id:"$components.prefix", count: {$sum: 1}}},
        {$sort: {count: -1}}
    ]);
    //print the result
    printjson(resultado._batch);
}