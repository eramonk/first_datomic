(ns first-datomic.core)

(require '[datomic.api :as d])

(def db-url "datomic:mem://db1")
;;(def db-url "datomic:free://localhost/db1")

(d/create-database db-url)

(def conn (d/connect db-url))

(def schema [{:db/ident       :person/name
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/id          (d/tempid :db.part/db)
              :db.install/_attribute :db.part/db}
             
             {:db/ident       :person/age
              :db/valueType   :db.type/long
              :db/cardinality :db.cardinality/one
              :db/index       true
              :db/id          (d/tempid :db.part/db)
              :db.install/_attribute :db.part/db}
             
             {:db/ident       :group/title
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/id          (d/tempid :db.part/db)
              :db.install/_attribute :db.part/db}
             
             {:db/ident       :group/students
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/many
              :db/id          (d/tempid :db.part/db)
              :db.install/_attribute :db.part/db}
             ])

@(d/transact conn schema)

@(d/transact conn [[:db/add (d/tempid :db.part/user) :person/name "Oleg"]])

@(d/transact conn [{:db/id (d/tempid :db.part/user)
                    :person/name "Ramon"
                    :person/age 33}]) 

@(d/transact conn [{:db/id (d/tempid :db.part/user)
                    :group/title "A"}])

(d/q '[:find  ?n ?a
       :where
       [?id :person/name ?n]
       [?id :person/age  ?a]]
     (d/db conn))

(let [db  (d/db conn)
      pid (ffirst (d/q '[:find ?p :where [?p :person/name "Ramon"]] db))
      p   (d/entity db pid)]
  (d/touch p))

(let [id (d/tempid :db.part/user)]
  id)




















