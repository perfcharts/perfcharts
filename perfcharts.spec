Name:           perfcharts
Version:        0.6.1
Release:        1%{?dist}
Summary:        Perfcharts is a tool for generating performance testing reports
BuildArch:	noarch

License:        GNU AFFERO GENERAL PUBLIC LICENSE
URL:            https://docs.engineering.redhat.com/x/hRfXAQ
Source0:        %{name}-%{version}.tar.gz

BuildRequires:  java-devel >= 1.7
BuildRequires:  ant
Requires:       java >= 1.7

%description
Perfcharts is a free software written in Java, which reads performance testing and system monitoring results from Jmeter, NMON, and/or other applications to produce charts for further analysis. It can generate any line and bar chart from any kind of data with appropriate extensions, but now is specially designed for performance testing business.

With this tool, you can get analysis charts by just putting a Jmeter result file (.jtl), some NMON resource monitoring logs (.nmon), and CPU load monitoring logs (.load) into a directory then just running the tool. This tool make it possible to enable automatic performance testing.


%prep
%setup -q -n %{name}-%{version}


%build
make build


%install
rm -rf $RPM_BUILD_ROOT
mkdir -p %{buildroot}%{_datadir}/%{name}/bin
cp -p build/dist/bin/* %{buildroot}%{_datadir}/%{name}/bin
mkdir -p %{buildroot}%{_datadir}/%{name}/res
cp -pr build/dist/res/* %{buildroot}%{_datadir}/%{name}/res
mkdir -p %{buildroot}%{_datadir}/%{name}/lib
cp -p build/dist/lib/* %{buildroot}%{_datadir}/%{name}/lib
mkdir -p %{buildroot}%{_bindir}
%{__ln_s} -f %{_datadir}/%{name}/bin/perfcharts %{buildroot}%{_bindir}/perfcharts
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-perf %{buildroot}%{_bindir}/cgt-perf
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-perf2 %{buildroot}%{_bindir}/cgt-perf2
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-cmp %{buildroot}%{_bindir}/cgt-cmp
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-js %{buildroot}%{_bindir}/cgt-js
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-monoreport %{buildroot}%{_bindir}/cgt-monoreport
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-trend %{buildroot}%{_bindir}/cgt-trend
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-zabbix-dl %{buildroot}%{_bindir}/cgt-zabbix-dl
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-timestamp %{buildroot}%{_bindir}/cgt-timestamp

%files
%doc README.md
%license LICENSE
%{_datadir}/%{name}/bin/*
%{_datadir}/%{name}/res/*
%{_datadir}/%{name}/lib/*
%{_bindir}/*
%post


%preun

%changelog
* Wed Nov 11 2015 Rayson Zhu <yuxzhu@redhat.com>
- release v0.6.0
- reconstruct

* Wed Nov 11 2015 Rayson Zhu <yuxzhu@redhat.com>
- release v0.5.2
- bugfix: parser crashes under some locale environment

