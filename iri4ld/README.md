# iri4ld

An implementation of RFC3986 and related RFCs focusing on parsing and checking for linked data and knowledge graph usage.

It provides [RFC 3986](https://datatracker.ietf.org/doc/html/rfc3986) URIs with
the character set extended to allow UTF-8 characters as defined in 
[RFC 3987](https://datatracker.ietf.org/doc/html/rfc3987) without requiring mapping
from IRIs to ASCII-URIs.

The project provides IRI syntax checking, parsing, resolution, normalization and relativizing.

[IRIs](https://www.w3.org/TR/rdf-concepts/#section-IRIs) are 
used in RDF ([IRI BNF](https://www.w3.org/TR/rdf12-concepts/#iri-abnf)).

The names "URI" and "IRI" are nowadays used synonymously and including moving
from ASCII to UTF-8 characters. This package refers to them as "IRI"s.

iri4ld provdes the algorithms specified in RFC 3986 for:
<ul>
<li>Checking a string matches the IRI grammar.
<li>Extracting components of an IRI
<li>Normalizing an IRI
<li>Resolving an IRI against a base IRI.
<li>Relativizing an IRI for a given base IRI.
<li>Rebuilding an IRI from components.
</ul>

### Code

The `IRI3986` class is the parsed IRI object.  Parsing RFC 3986 syntax allocates
a single Java object. Checking for scheme specific rules is also provided.

The class `RFC3986` class provides the starting point for creating and operating
on `IRI3986` objects.

### Usage

#### Check
Check conformance with the RFC 3986 grammar:
```
    RFC3986.checkSyntax(string);
```
Check conformance with the RFC 3986 grammar and any applicable scheme specific rules:
```
    IRI3986 iri = RFC3986.create(string);
    iri.hasViolations();
```
#### Extract the components of IRI
```
    IRI3986 iri = RFC3986.create(string);
    String scheme = iri.schema();
      ... iri.path();
      ...
```
#### Resolve
```
    IRI3986 base = RFC3986.create(baseIRIString);
    IRI3986 iri  = RFC3986.create(string);
    IRI3986 iri2 = RFC3986.resolve(base);
```
#### Normalize
```
    IRI3986 iri  = RFC3986.create(string);
    IRI3986 iri2 = RFC3986.normalize(iri);
```
#### Relative IRI
```
    IRI3986 base = RFC3986.create(baseIRIString);
    IRI3986 target = RFC3986.create(string);
    IRI3986 relative = RFC3986.relativize(base, target);
    // then base.resolve(relative) equals target
```

### RFC Regular Expression

An IRI can be created using the regular expression
of RFC 3986. This regular expression identifies the components
without checking for correct use of characters within components.
It may be useful when an IRI does not conform to the details of the
RFC 3986 syntax, for example spaces in the path component.

### Supported URI schemes
| Scheme | Definition |
| ------ | -----------|
| `http:`, `https:` | [RFC 7230](https://datatracker.ietf.org/doc/html/rfc7230) |
| `did:`            | [W3C DID Core](https://www.w3.org/TR/did-core/) |
| `file:`           | [RFC 8089](https://datatracker.ietf.org/doc/html/rfc8089) |
| `urn:`            | [RFC 8141](https://datatracker.ietf.org/doc/html/rfc8141) |
| `urn:uuid:`       | [RFC 4122](https://datatracker.ietf.org/doc/html/rfc4122) |
| `urn:oid:`        | [RFC 3061](https://datatracker.ietf.org/doc/html/rfc3061) |
| `example:`  | [RFC 7595, section 8](https://datatracker.ietf.org/doc/html/rfc7595#section-8) |

#### Non-standard schemes
| Scheme | Definition |
| ------ | -----------|
| `uuid:` | NID and NSS part of `urn:uuid:...`, no URN components |
| `oid:` | NID and NSS part of `urn:oid:...`, no URN components |

See also:
* [RFC 3986](https://datatracker.ietf.org/doc/html/rfc3986)
* [RFC 3987](https://datatracker.ietf.org/doc/html/rfc3987)
* [HTML5 URLs](https://dev.w3.org/html5/spec-LC/urls.html#urls)
* [IANA URI scheme registry](https://www.iana.org/assignments/uri-schemes/uri-schemes.xhtml)
* [IANA URN namespace registry](https://www.iana.org/assignments/urn-namespaces/urn-namespaces.xhtml)

### Command line

Print details of an IRI:

`java -cp target/classes org.seaborne.rfc3986.cmd.CmdIRI iri-string`

Apply the resolution algotithm to a base IRI and another IRI:

`java -cp target/classes org.seaborne.rfc3986.cmd.ResolveIRI`

### Obtaining

Download [the code](https://github.com/afs/x4ld) and build with `mvn clean install`.

### Building

`mvn clean install`

The runtime artifact does not have any java dependencies. In particular, it does
not depend on any [Apache Jena](https://jena./apche.org/) artifacts and can be
used independently of Jena.

The test suite depends on
[jena-iri](https://github.com/apache/jena/tree/main/jena-iri) to compare IRI
parsing and URI scheme coverage.
